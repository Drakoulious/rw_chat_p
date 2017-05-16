package ru.ilonich.roswarcp.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import ru.ilonich.roswarcp.model.CheckedProfile;
import ru.ilonich.roswarcp.model.Message;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class Parser {

    private static final ObjectMapper mapper;
    private static final String SYSTEM_TYPE = "system";
    private static final Function<Message, Message> reformatSystemMessage = (message -> {
        Document doc = Jsoup.parse(message.getText());
        if (!doc.select("span").isEmpty()) {
            message.setFraction(doc.select("span.user i").attr("title"));
            Elements clanSpan = doc.select("span.user a[href^=/clan/]");
            if (!clanSpan.isEmpty()) {
                message.setClanId(Integer.valueOf(clanSpan.attr("href").replaceAll("/", "").substring(4)));
                message.setClanName(doc.select("img.clan-icon").attr("title"));
            }
            message.setNickName(doc.select("span.user a[href^=/player/]").text());
            message.setLinkedPlayerId(message.getPlayerId()); //газетчик делает объявления обычно
            message.setPlayerId(Integer.valueOf(doc.select("span.user a[href^=/player/]").attr("href").replaceAll("/", "").substring(6)));
            message.setLevel(Integer.valueOf(doc.select("span.level").text().replaceAll("[^\\d]", "")));
            message.setItemName(doc.select("div.objects div.padding img[src$=.png").attr("alt"));
            message.setItemCount(Integer.valueOf(doc.select("div.count").text().substring(1)));
            message.setText(doc.text());
        }
        return message;
    });

    static {
        mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        SimpleModule module = new SimpleModule();
        module.addDeserializer(List.class, new MessageDeserializer());
        mapper.registerModule(module);
    }
    private Parser(){
    }

    public static List<Message> getMessagesFromChat() {
        if (CurrentState.getStatus() == CurrentState.State.ON) {
            ChatMessagesRequest request = buildChatMessagesRequest();
            if (request != null) {
                String json = request.requestMessages();
                if (json != null){
                    List<Message> messages = parseJson(json);
                    if (!messages.isEmpty()){
                        updateCurrentState(findLastMessage(messages), request);
                        return findSystemMessages(messages);
                    }
                }
            }
        }
        return null;
    }

    private static List<Message> findSystemMessages(List<Message> messages) {
        return messages.stream()
                .filter( m -> m.getType().equals(SYSTEM_TYPE) && m.getPlayerId() == 131716) //убрать сообщения о банах и боях, только газетчик пишет нужное
                .map(reformatSystemMessage)
                .collect(Collectors.toList());
    }

    private static void updateCurrentState(Message lastMessage, ChatMessagesRequest pastRequest) {
        CurrentState.setLastMessageId(pastRequest.getLastMessageId(), lastMessage.getId());
        CurrentState.setLastMessageType(pastRequest.getType(), lastMessage.getType().equals(SYSTEM_TYPE) ? SYSTEM_TYPE : "");
    }

    private static Message findLastMessage(List<Message> messages) {
        return messages.stream().max( (m1, m2) -> Integer.compare(m1.getId(), m2.getId()) ).get();
    }

    private static List<Message> parseJson(String json) {
        try {
            return mapper.readValue(json, List.class);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return Collections.EMPTY_LIST;
    }

    private static ChatMessagesRequest buildChatMessagesRequest() {
        String cookiesValue = CurrentState.getCookiesValue();
        if (cookiesValue != null) {
            return new ChatMessagesRequest(CurrentState.getLastMessageId(),
                    CurrentState.getLastMessageType(), cookiesValue);
        } else {
            return null;
        }
    }
}
