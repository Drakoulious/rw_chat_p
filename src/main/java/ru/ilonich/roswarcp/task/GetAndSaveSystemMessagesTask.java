package ru.ilonich.roswarcp.task;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ilonich.roswarcp.client.ChatMessagesRequest;
import ru.ilonich.roswarcp.client.CurrentState;
import ru.ilonich.roswarcp.client.Parser;
import ru.ilonich.roswarcp.model.CheckedProfile;
import ru.ilonich.roswarcp.model.Message;
import ru.ilonich.roswarcp.repo.CheckedProfileMapper;
import ru.ilonich.roswarcp.repo.MessageMapper;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetAndSaveSystemMessagesTask {

    private static final String PROFILE_URL = "http://www.roswar.ru/player/%d/";
    private static final Map<String, String> HEADERS = new HashMap<>();
    static {
        HEADERS.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        HEADERS.put("Accept-Encoding", "gzip, deflate, sdch");
        HEADERS.put("Accept-Language", "ru-RU,ru;q=0.8,en-US;q=0.6,en;q=0.4");
        HEADERS.put("Connection", "keep-alive");
        HEADERS.put("Upgrade-Insecure-Requests", "1");
    }
    private static final Pattern GIFT_RECEIVED_PATTERN = Pattern.compile("Подарен: (\\d{2}.\\d{2}.\\d{4})[\\s\\S]*(\\d{2}:\\d{2})[\\s\\S]*Годен до:");

    private Logger LOG = LoggerFactory.getLogger(GetAndSaveSystemMessagesTask.class);

    private MessageMapper messageMapper;

    private CheckedProfileMapper cpMapper;

    private List<String> triggerItems;

    public GetAndSaveSystemMessagesTask() {
    }

    public void task(){
        List<Message> messages = Parser.getMessagesFromChat();
        if (messages != null && !messages.isEmpty()){
            messageMapper.insertMessages(messages);
            checkForTriggerItems(messages);
        }
    }

    private void checkForTriggerItems(List<Message> messages) {
        messages.forEach( m -> {
            String itemName = m.getItemName();
            triggerItems.forEach( tin -> {
                if (itemName.equalsIgnoreCase(tin)) checkPlayerInventory(m);
            });
        });
    }

    private void checkPlayerInventory(Message trigger) {
        try {
            HEADERS.put("Cookie", CurrentState.getCookiesValue());
            Document doc = Jsoup.connect(String.format(PROFILE_URL, trigger.getPlayerId())).headers(HEADERS).userAgent(ChatMessagesRequest.USER_AGENT).get();
            Element el = doc.select("div#gifts2 img").first();
            int lastPrizeDataId = Integer.parseInt(el.attr("data-id"));
            Matcher matcher = GIFT_RECEIVED_PATTERN.matcher(el.attr("title"));
            if (matcher.find())
            {
                Timestamp lastPrizeDate = Timestamp.valueOf(LocalDateTime.parse(String.format("%sT%s", matcher.group(1), matcher.group(2)),
                        DateTimeFormatter.ofPattern("dd.MM.yyyy'T'HH:mm")));
                cpMapper.save(new CheckedProfile(trigger.getPlayerId(), trigger.getId(), lastPrizeDataId, lastPrizeDate));
            }
        } catch (IOException e){
            LOG.warn("Failed to check player profile #%d ; IOException: %s", trigger.getPlayerId(), e.getMessage());
        } catch (Exception e) {
            LOG.warn(String.format("Failed to parse player #%d last gift", trigger.getPlayerId()), e);
        }
    }

    public void setMessageMapper(MessageMapper messageMapper) {
        this.messageMapper = messageMapper;
    }

    public void setCpMapper(CheckedProfileMapper cpMapper) {
        this.cpMapper = cpMapper;
    }

    public void setTriggerItems(List<String> triggerItems) {
        this.triggerItems = triggerItems;
    }
}
