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
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
    private List<Integer> clansToScan;
    private static final String BONUS_PAGE_URL = "http://www.roswar.ru/holiday/";
    private static final String CLAN_PAGE_URL = "http://www.roswar.ru/clan/%d/";

    public void task(){
        if (CurrentState.getStatus() != CurrentState.State.ON) return;
        long start = System.currentTimeMillis();
        GiftsSnapshot snapshot = giftsMapper.getLast();
        final GiftsSnapshot lastSnapshot = snapshot == null ? new GiftsSnapshot(0, 0, Timestamp.from(Instant.now()), 0, 0, 0) : snapshot; //нулевые значения если в бд не было записи
        int lastHornId = getLastHornId(); // смотрим id последнего рога в бд
        int hornOwnerId = getLuckyPlayerId(); //смотрим последнего счастливчика на сайте
        if (isNewLucker(hornOwnerId)){ // если это не тот же самый чел что получен из бд (предполагается что несколько раз подряд одному игроку рог не выпадает)
            HornInfo hornInfo = getLuckyPlayerHornInfo(hornOwnerId); // получим data-id предпологаемого рога (с записью новой строки в таблицу)
            lastHornId = hornInfo != null ? hornInfo.getDataId() : 0; //
        }

        int countNew;
        int lastGiftId;
        int dataIdsDelta;
        long timeDelta;
        int hornDelta;
        List<Integer> playersToScan = getClanMembersIds(clansToScan);
        List<Integer> mayGiftsDataIds = new ArrayList<>();
        for (int playerId : playersToScan){
            mayGiftsDataIds.addAll(parseGiftsIdsFromPlayerPage(playerId));
        }

        List<Integer> newGifts = mayGiftsDataIds.stream().parallel()
                .filter( giftId -> giftId > lastSnapshot.getLastGiftId())
                .collect(Collectors.toList());

        countNew = newGifts.size();

        if (countNew == 0){ //если ничё нового в подарках
            lastGiftId = lastSnapshot.getLastGiftId() == 0 ? lastHornId : lastSnapshot.getLastGiftId();
        } else {
            lastGiftId = newGifts.stream().mapToInt(Integer::intValue).max().getAsInt();
        }

        if (lastSnapshot.getLastGiftId() == 0){
            dataIdsDelta = lastGiftId - lastHornId;
        } else {
            dataIdsDelta = lastGiftId - lastSnapshot.getLastGiftId();
        }
        hornDelta = lastGiftId - lastHornId;
        Instant currentSnapshotTime = Instant.now();
        timeDelta = currentSnapshotTime.getEpochSecond() - lastSnapshot.getTime().toInstant().getEpochSecond();
        giftsMapper.insertRow(new GiftsSnapshot(countNew, lastGiftId, Timestamp.from(currentSnapshotTime), dataIdsDelta, timeDelta, hornDelta));
        long stop = System.currentTimeMillis();
        long parsingTime = stop - start;
        LOG.info("Task executed: \n Last player ID={} with horn ID={} \n Previous nax gift ID={} new={} \n {} new gifts founded \n Time estimated: {} \n Players scanned: {} \n Overall 'may' gifts: {}", hornOwnerId, lastHornId, lastSnapshot.getLastGiftId(), lastGiftId, countNew, parsingTime, playersToScan.size(), mayGiftsDataIds.size());
    }

    @SuppressWarnings("unchecked")
    private List<Integer> parseGiftsIdsFromPlayerPage(int playerId) {
        try {
            HEADERS.put("Cookie", CurrentState.getCookiesValue());
            Document doc = Jsoup.connect(String.format(PROFILE_URL, playerId)).headers(HEADERS).userAgent(ChatMessagesRequest.USER_AGENT).get();
            Elements els = doc.select("div#gifts2 img");
            return Stream.of(els)
                    .flatMap(elements -> elements.subList(0, elements.size()).stream())
                    .filter(element -> element.attr("src").contains("gift-may2017")) //только ивентовые гифты
                    .map(element -> Integer.valueOf(element.attr("data-id")))
                    .collect(Collectors.toList());
        } catch (IOException e){
            LOG.warn("Failed to check player id:#%d gifts; IOException: %s", playerId, e.getMessage());
        }
        return Collections.EMPTY_LIST;
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
            Elements els = doc.select("div#gifts2 img");
            Element firstMayGift = null;
            for (Element el : els){
                String imgSrcValue = el.attr("src");
                if (imgSrcValue.contains("gift-may2017")){
                    firstMayGift = el;
                    break;
                } else {
                    firstMayGift = el;
                }
            }
            int lastPrizeDataId = Integer.parseInt(firstMayGift.attr("data-id"));
            Matcher matcher = GIFT_RECEIVED_PATTERN.matcher(firstMayGift.attr("title"));
            if (matcher.find())
            {
                Timestamp lastPrizeDate = Timestamp.from(LocalDateTime.parse(String.format("%sT%s", matcher.group(1), matcher.group(2)),
                        DateTimeFormatter.ofPattern("dd.MM.yyyy'T'HH:mm")).toInstant(ZoneOffset.MIN));
                String sql = "INSERT INTO horns (player_id, data_id, time) VALUES (%d, %d, '%s')";
                rawSql.executeDirectQuery(String.format(sql, hornOwnerId, lastPrizeDataId, lastPrizeDate.toString()));
                return new HornInfo(hornOwnerId, lastPrizeDataId, lastPrizeDate);
            }
        } catch (IOException e){
            LOG.warn("Failed to check player profile #%d ; IOException: %s", hornOwnerId, e.getMessage());
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
            LOG.warn("Failed to parse clan players ; Exception: %s", e.getMessage(), e);
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
            LOG.warn("Failed to check bonus page ; IOException: %s", e.getMessage());
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

        public void setPlayerId(int playerId) {
            this.playerId = playerId;
        }

        public int getDataId() {
            return dataId;
        }

        public void setDataId(int dataId) {
            this.dataId = dataId;
        }

        public Timestamp getTime() {
            return time;
        }

        public void setTime(Timestamp time) {
            this.time = time;
        }
    }

    public void setGiftsMapper(GiftsMapper giftsMapper) {
        this.giftsMapper = giftsMapper;
    }

    public void setRawSql(RawSqlExecutor rawSql) {
        this.rawSql = rawSql;
    }

    public void setClansToScan(List<Integer> clansToScan) {
        this.clansToScan = clansToScan;
    }
}
