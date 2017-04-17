package ru.ilonich.roswarcp.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import ru.ilonich.roswarcp.model.Message;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Илоныч on 13.04.2017.
 */
public final class Parser {

    private static final ObjectMapper mapper;
    private static final String SYSTEM_TYPE = "system";

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
                HttpResponse response = request.requestMessages();
                if (response != null){
                    String json = getJson(response);
                    if (json != null){
                        List<Message> messages = parseJson(json);
                        if (!messages.isEmpty()){
                            updateCurrentState(findLastMessage(messages), request);
                            return findSystemMessages(messages);
                        }
                    }
                }
            }
        }
        return null;
    }

    private static List<Message> findSystemMessages(List<Message> messages) {
        return messages.stream()
                .filter( m -> m.getType().equals(SYSTEM_TYPE))
                .collect(Collectors.toList());
        //здеся можно распарсить html системных сообщений
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

    private static String getJson(HttpResponse response) {
        try {
            return IOUtils.toString(response.getEntity().getContent(), Charset.defaultCharset());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private static ChatMessagesRequest buildChatMessagesRequest() {
        try {
            return new ChatMessagesRequest(CurrentState.getLastMessageId(),
                    CurrentState.getLastMessageType(), CurrentState.getCookiesValue());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            CurrentState.setStatus(CurrentState.State.BAD_RESULT);
            CurrentState.setLogin("none");
        }
        return null;
    }
}
