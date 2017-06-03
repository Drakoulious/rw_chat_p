package ru.ilonich.roswarcp.task;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ilonich.roswarcp.client.ChatMessagesRequest;
import ru.ilonich.roswarcp.client.CurrentState;
import ru.ilonich.roswarcp.model.GiftsSnapshot;
import ru.ilonich.roswarcp.repo.GiftsMapper;
import ru.ilonich.roswarcp.repo.RawSqlExecutor;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.ilonich.roswarcp.task.GetAndSaveSystemMessagesTask.GIFT_RECEIVED_PATTERN;
import static ru.ilonich.roswarcp.task.GetAndSaveSystemMessagesTask.HEADERS;
import static ru.ilonich.roswarcp.task.GetAndSaveSystemMessagesTask.PROFILE_URL;

/**
 * Created by Никола on 30.05.2017.
 */
public class CheckClanPlayersTask {
    private Logger LOG = LoggerFactory.getLogger(CheckClanPlayersTask.class);
    private GiftsMapper giftsMapper;
    private RawSqlExecutor rawSql;
    private List<Integer> playersToScan;
    private static final List<Integer> PLAYERS_TO_REMOVE = new CopyOnWriteArrayList<>();
    private static final AtomicInteger GENERAL_LAST_GIFT_ID = new AtomicInteger(0);
    private static final Map<Integer, Integer> LAST_GIFTS_IDS = new ConcurrentHashMap<>(1000);
    private static final String BONUS_PAGE_URL = "http://www.roswar.ru/holiday/";
    private static final String CLAN_PAGE_URL = "http://www.roswar.ru/clan/%d/";
    private static final String RATING_PAGE_URL = "http://www.roswar.ru/rating/moneygrabbed/month/%d/";

    public void task(){
        if (CurrentState.getStatus() != CurrentState.State.ON) return;
        LOG.info("Parsing...");
        long start = System.currentTimeMillis();

        int lastHornId = getLastHornId(); // смотрим id последнего рога в бд
        int hornOwnerId = getLuckyPlayerId(); //смотрим последнего счастливчика на сайте
        if (isNewLucker(hornOwnerId)){ // если это не тот же самый чел что получен из бд (предполагается что несколько раз подряд одному игроку рог не выпадает)
            HornInfo hornInfo = getLuckyPlayerHornInfo(hornOwnerId); // получим data-id предпологаемого рога (с записью новой строки в таблицу)
            lastHornId = hornInfo != null ? hornInfo.getDataId() : 0; //
        }
        GiftsSnapshot lastSnapshot = giftsMapper.getLast();
        lastSnapshot = lastSnapshot == null ? new GiftsSnapshot(0, lastHornId, Timestamp.from(Instant.now()),
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0) : lastSnapshot; //id последнего рога и нулевые значения на остальное, если в бд не было записи
        GENERAL_LAST_GIFT_ID.set(lastSnapshot.getLastGiftId());

        if (playersToScan == null){
            playersToScan = new ArrayList<>(getTop1000PlayersIds());
        }

        //Non-parallel
        //List<PlayerInventory> scanResult = playersToScan.stream().map(this::parsePlayerPage).collect(Collectors.toList());
        Stream<PlayerInventory> stream = playersToScan.parallelStream().map(this::parsePlayerPage);
        Callable<List<PlayerInventory>> task = () -> stream.collect(Collectors.toList());
        ForkJoinPool forkJoinPool = new ForkJoinPool(4);
        List<PlayerInventory> scanResult = null;
        try {
            scanResult = forkJoinPool.submit(task).get();
        } catch (InterruptedException | ExecutionException e) {
            LOG.warn("Brain parallelism exception", e);
            scanResult = Collections.EMPTY_LIST;
        }

        List<PlayerGiftsInfo> parsed = scanResult.parallelStream()
                .map(this::inspectInventory)
                .collect(Collectors.toList());

        List<Integer> newGifts = new ArrayList<>();
        int smallGiftsCount = 0;
        int mediumGiftsCount = 0;
        int bigGiftsCount = 0;
        int notOpenedGiftsCount = 0;
        int _smallGiftsCount = 0;
        int _mediumGiftsCount = 0;
        int _bigGiftsCount = 0;
        for (PlayerGiftsInfo info : parsed){
            newGifts.addAll(info.getGiftIds());
            smallGiftsCount += info.getSmallGiftsCount();
            mediumGiftsCount += info.getMediumGiftsCount();
            bigGiftsCount += info.getBigGiftsCount();
            notOpenedGiftsCount += info.getNotOpenedGiftsCount();
            _smallGiftsCount += info.get_smallGiftsCount();
            _mediumGiftsCount += info.get_mediumGiftsCount();
            _bigGiftsCount += info.get_bigGiftsCount();
        }

        int countNew;
        int lastGiftId;
        int dataIdsDelta;
        long timeDelta;
        int hornDelta;

        countNew = newGifts.size();
        if (countNew == 0){ //если ничё нового в подарках
            lastGiftId = GENERAL_LAST_GIFT_ID.get();
            dataIdsDelta = 0;
        } else {
            lastGiftId = newGifts.stream().mapToInt(Integer::intValue).max().getAsInt();
            dataIdsDelta = lastGiftId - GENERAL_LAST_GIFT_ID.get();
        }

        hornDelta = lastGiftId - lastHornId;
        Instant currentSnapshotTime = Instant.now();
        timeDelta = currentSnapshotTime.getEpochSecond() - lastSnapshot.getTime().toInstant().getEpochSecond();

        giftsMapper.insertRow(new GiftsSnapshot(countNew, lastGiftId, Timestamp.from(currentSnapshotTime), dataIdsDelta, timeDelta, hornDelta,
                smallGiftsCount, mediumGiftsCount, bigGiftsCount, notOpenedGiftsCount, _smallGiftsCount, _mediumGiftsCount, _bigGiftsCount));

        long stop = System.currentTimeMillis();
        long parsingTime = stop - start;

        LOG.info("\n Task executed: \n Last player ID={} with horn ID={} \n Previous max gift ID={}, new={} \n {} new gifts founded \n Time estimated: {} \n Players scanned: {} \n Saved last data-ids: {} \n",
                hornOwnerId, lastHornId, lastSnapshot.getLastGiftId(), lastGiftId, countNew, parsingTime, playersToScan.size(), LAST_GIFTS_IDS.size());

        playersToScan.removeAll(PLAYERS_TO_REMOVE);
        PLAYERS_TO_REMOVE.clear();
    }


    private PlayerInventory parsePlayerPage(int playerId) {
        try {
            HEADERS.put("Cookie", CurrentState.getCookiesValue());
            Document doc = Jsoup.connect(String.format(PROFILE_URL, playerId)).headers(HEADERS).userAgent(ChatMessagesRequest.USER_AGENT).get();
            Elements playerGifts = doc.select("div#gifts2 img");
            Elements mayGifts = playerGifts.select("img[src*=gift-may2017]");//только ивентовые гифты
            if (playerGifts.size() < 100){ //если меньше 100 штук и нет майских то исключаем
                if (mayGifts.size() == 0){
                    return new PlayerInventory(playerId, null, true);
                }
            }
            return new PlayerInventory(playerId, mayGifts, false);
        } catch (IOException e){
            LOG.warn("Failed to check player id:{} gifts; IOException: {}", playerId, e.getMessage());
            return new PlayerInventory(playerId, null, false);
        }
    }

    @SuppressWarnings("unchecked")
    private PlayerGiftsInfo inspectInventory(PlayerInventory inventory){
        int idFilter = LAST_GIFTS_IDS.getOrDefault(inventory.getPlayerId(), GENERAL_LAST_GIFT_ID.get());
        //LOG.trace("Switching was {}/{} \n", GENERAL_LAST_GIFT_ID.get(), idFilter);
        AtomicInteger nextIdFilter = new AtomicInteger(0);
        AtomicInteger smallGiftsCount = new AtomicInteger(0);
        AtomicInteger mediumGiftsCount = new AtomicInteger(0);
        AtomicInteger bigGiftsCount = new AtomicInteger(0);
        AtomicInteger _smallGiftsCount = new AtomicInteger(0);
        AtomicInteger _mediumGiftsCount = new AtomicInteger(0);
        AtomicInteger _bigGiftsCount = new AtomicInteger(0);
        AtomicInteger notOpenedGiftsCount = new AtomicInteger(0);
        Elements gifts = inventory.getInventory();
        if (gifts == null){
            if (inventory.isSkip()){
                PLAYERS_TO_REMOVE.add(inventory.getPlayerId());
            }
            return new PlayerGiftsInfo(inventory.getPlayerId(), 0, 0, 0, Collections.EMPTY_LIST, 0, 0, 0, 0);
        }
        List<Integer> newGifts = Stream.of(gifts)
                .flatMap(elements -> elements.subList(0, elements.size()).stream())
                .filter(element -> {
                    int dataId = Integer.parseInt(element.attr("data-id"));
                    if (dataId > nextIdFilter.get()) nextIdFilter.set(dataId);
                    return dataId > idFilter; //только новые
                })
                .peek(element -> { String dataSt = element.attr("data-st");
                    switch (dataSt) {
                        case "10033":
                            smallGiftsCount.incrementAndGet();
                            break;
                        case "10034":
                            mediumGiftsCount.incrementAndGet();
                            break;
                        case "10035":
                            bigGiftsCount.incrementAndGet();
                            break;
                        case "10023":
                            _smallGiftsCount.incrementAndGet();
                            break;
                        case "10024":
                            _mediumGiftsCount.incrementAndGet();
                            break;
                        case "10025":
                            _bigGiftsCount.incrementAndGet();
                            break;
                    }
                    if (element.attr("data-unlocked").charAt(0) != '1'){
                        notOpenedGiftsCount.incrementAndGet();
                    }
                })
                .map(element -> Integer.valueOf(element.attr("data-id")))
                .collect(Collectors.toList());
        LAST_GIFTS_IDS.put(inventory.getPlayerId(), nextIdFilter.get());
        return new PlayerGiftsInfo(inventory.getPlayerId(), smallGiftsCount.get(), mediumGiftsCount.get(), bigGiftsCount.get(), newGifts, notOpenedGiftsCount.get(), _smallGiftsCount.get(), _mediumGiftsCount.get(), _bigGiftsCount.get());
    }

    private int getLastHornId() {
        try {
            String hornId = rawSql.executeQuery("SELECT data_id FROM horns ORDER BY line_num DESC LIMIT 1").get(1).get(0);
            return Integer.parseInt(hornId);
        } catch (IndexOutOfBoundsException e){
            return 0;
        }
    }

    private boolean isNewLucker(int hornOwnerId) {
        try {
            String lastLuckerId = rawSql.executeQuery("SELECT player_id FROM horns ORDER BY line_num DESC LIMIT 1").get(1).get(0);
            return Integer.compare(hornOwnerId, Integer.parseInt(lastLuckerId)) != 0;
        } catch (IndexOutOfBoundsException e){
            return true;
        }
    }

    private HornInfo getLuckyPlayerHornInfo(int hornOwnerId) {
        try {
            HEADERS.put("Cookie", CurrentState.getCookiesValue());
            Document doc = Jsoup.connect(String.format(PROFILE_URL, hornOwnerId)).headers(HEADERS).userAgent(ChatMessagesRequest.USER_AGENT).get();
            Element firstMayGift = doc.select("div#gifts2 img[src*=gift-may2017]").first();
            if (firstMayGift == null){
                firstMayGift = doc.select("div#gifts2 img").first();
            }
            int lastPrizeDataId = Integer.parseInt(firstMayGift.attr("data-id"));
            Matcher matcher = GIFT_RECEIVED_PATTERN.matcher(firstMayGift.attr("title"));
            if (matcher.find())
            {
                Timestamp lastPrizeDate = Timestamp.valueOf(String.format("%s-%s-%s %s:%s:00", matcher.group(3), matcher.group(2), matcher.group(1),
                        matcher.group(4), matcher.group(5)));
                String sql = "INSERT INTO horns (player_id, data_id, time) VALUES (%d, %d, '%s')";
                rawSql.executeDirectQuery(String.format(sql, hornOwnerId, lastPrizeDataId, lastPrizeDate.toString()));
                return new HornInfo(hornOwnerId, lastPrizeDataId, lastPrizeDate);
            }
        } catch (IOException e){
            LOG.warn("Failed to check player profile {} ; IOException: {}", hornOwnerId, e.getMessage());
        } catch (Exception e) {
            LOG.warn(String.format("Failed to parse player #%d last gift", hornOwnerId), e);
        }
        return null;
    }

    private List<Integer> getClanMembersIds(List<Integer> clansToScan) {
        List<Integer> result = new ArrayList<>();
        try {
            for (int clanId : clansToScan){
                HEADERS.put("Cookie", CurrentState.getCookiesValue());
                Document doc = Jsoup.connect(String.format(CLAN_PAGE_URL, clanId)).headers(HEADERS).userAgent(ChatMessagesRequest.USER_AGENT).get();
                Elements elements = doc.select("div#players a");
                for (Element el : elements){
                    if (el.hasText()){
                        result.add(Integer.parseInt(el.attr("href").replaceAll("\\D", "")));
                    }
                }
            }
        } catch (Exception e){
            LOG.warn("Failed to parse clan players; Exception: {}", e.getMessage(), e);
        }
        return result;
    }

    private List<Integer> getTop1000PlayersIds() {
        List<Integer> result = new CopyOnWriteArrayList<>();
        HEADERS.put("Cookie", CurrentState.getCookiesValue());
        ExecutorService service = Executors.newFixedThreadPool(4);
        try {
            for (int i = 1; i <= 50; i++){
                int num = i;
                service.submit(() -> {
                    Document doc = null;
                    try {
                        doc = Jsoup.connect(String.format(RATING_PAGE_URL, num)).headers(HEADERS).userAgent(ChatMessagesRequest.USER_AGENT).get();
                        Elements els = doc.select("tr:not(.my) td span.user a[href^=/player/]");
                        els.forEach(e -> {result.add(Integer.valueOf(e.attr("href").replaceAll("\\D", "")));});
                    } catch (IOException e) {
                        LOG.warn("Failed to parse top 1000 players; {}", e.getMessage());
                    }
                });
            }
            service.shutdown();
            service.awaitTermination(120, TimeUnit.SECONDS);
        } catch (Exception e){
            LOG.warn("Failed to parse top 1000 players; Exception: {}", e.getMessage());
        }
        return result;
    }

    private int getLuckyPlayerId() {
        try {
            HEADERS.put("Cookie", CurrentState.getCookiesValue());
            Document doc = Jsoup.connect(BONUS_PAGE_URL).headers(HEADERS).userAgent(ChatMessagesRequest.USER_AGENT).get();
            String el = doc.select("div.banner-may__pers-name span.user a").attr("href");
            return Integer.parseInt(el.replaceAll("\\D", ""));
        } catch (IOException e){
            LOG.warn("Failed to check bonus page; IOException: {}", e.getMessage());
        } catch (Exception e) {
            LOG.warn("Failed to parse player id on bonus page");
        }
        return 0;
    }

    private class HornInfo {
        private int playerId;
        private int dataId;
        private Timestamp time;

        public HornInfo(int playerId, int dataId, Timestamp time) {
            this.playerId = playerId;
            this.dataId = dataId;
            this.time = time;
        }

        public int getPlayerId() {
            return playerId;
        }

        public int getDataId() {
            return dataId;
        }

        public Timestamp getTime() {
            return time;
        }

        public void setTime(Timestamp time) {
            this.time = time;
        }
    }

    private class PlayerInventory{
        private int playerId;
        private Elements inventory;
        private boolean skip;
        public PlayerInventory(int playerId, Elements inventory, boolean skip) {
            this.playerId = playerId;
            this.inventory = inventory;
            this.skip = skip;
        }
        public int getPlayerId() {
            return playerId;
        }
        public Elements getInventory() {
            return inventory;
        }
        public boolean isSkip() {
            return skip;
        }
    }

    private class PlayerGiftsInfo {
        private int playerId;
        private int smallGiftsCount;
        private int mediumGiftsCount;
        private int bigGiftsCount;
        private List<Integer> giftIds;
        private int notOpenedGiftsCount;
        private int _smallGiftsCount;
        private int _mediumGiftsCount;
        private int _bigGiftsCount;

        public PlayerGiftsInfo(int playerId, int smallGiftsCount, int mediumGiftsCount, int bigGiftsCount, List<Integer> giftIds, int notOpenedGiftsCount, int smallGiftsCount1, int mediumGiftsCount1, int bigGiftsCount1) {
            this.playerId = playerId;
            this.smallGiftsCount = smallGiftsCount;
            this.mediumGiftsCount = mediumGiftsCount;
            this.bigGiftsCount = bigGiftsCount;
            this.giftIds = giftIds;
            this.notOpenedGiftsCount = notOpenedGiftsCount;
            this._smallGiftsCount = smallGiftsCount1;
            this._mediumGiftsCount = mediumGiftsCount1;
            this._bigGiftsCount = bigGiftsCount1;
        }

        public int getSmallGiftsCount() {
            return smallGiftsCount;
        }

        public int getMediumGiftsCount() {
            return mediumGiftsCount;
        }

        public int getBigGiftsCount() {
            return bigGiftsCount;
        }

        public List<Integer> getGiftIds() {
            return giftIds;
        }

        public int getPlayerId() {
            return playerId;
        }

        public int getNotOpenedGiftsCount() {
            return notOpenedGiftsCount;
        }

        public int get_smallGiftsCount() {
            return _smallGiftsCount;
        }

        public int get_mediumGiftsCount() {
            return _mediumGiftsCount;
        }

        public int get_bigGiftsCount() {
            return _bigGiftsCount;
        }
    }

    public void setGiftsMapper(GiftsMapper giftsMapper) {
        this.giftsMapper = giftsMapper;
    }

    public void setRawSql(RawSqlExecutor rawSql) {
        this.rawSql = rawSql;
    }
}
