package ru.ilonich.roswarcp.client;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

/**
 * Created by Илоныч on 13.04.2017.
 */
public final class Parser {
    private Parser(){
    }

    public static void getMessagesFromChat() {
        System.out.println("пытаюсь");
        if (CurrentState.getStatus() == CurrentState.State.ON) {
            ChatMessagesRequest request = new ChatMessagesRequest(CurrentState.getLastMessageId(),
                    CurrentState.getLastMessageType(), CurrentState.getCookiesValue());
            try {
                HttpResponse response = request.requestMessages();
                System.out.println(IOUtils.toString(response.getEntity().getContent()));
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
