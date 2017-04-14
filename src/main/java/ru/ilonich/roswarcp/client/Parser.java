package ru.ilonich.roswarcp.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import ru.ilonich.roswarcp.model.Message;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Илоныч on 13.04.2017.
 */
public final class Parser {

    private static ObjectMapper mapper;

    static {
        mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Message.class, new MessageDeserializer());
        mapper.registerModule(module);
    }
    private Parser(){
    }

    public static void getMessagesFromChat() {
        System.out.println("пытаюсь");
        if (CurrentState.getStatus() == CurrentState.State.ON) {
            ChatMessagesRequest request = new ChatMessagesRequest(CurrentState.getLastMessageId(),
                    CurrentState.getLastMessageType(), CurrentState.getCookiesValue());
            try {
                HttpResponse response = request.requestMessages();
                String json = IOUtils.toString(response.getEntity().getContent());
                List<Message> messages = mapper.readValue(json, List.class);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        try {
            TimeUnit.SECONDS.sleep(30);
        } catch (InterruptedException e) {
        }
    }
}
