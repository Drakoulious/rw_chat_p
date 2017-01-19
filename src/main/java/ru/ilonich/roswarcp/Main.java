package ru.ilonich.roswarcp;

import ru.ilonich.roswarcp.client.ChatMessagesRequest;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;

import java.io.IOException;
import java.util.stream.Stream;

/**
 * Created by Никола on 12.01.2017.
 */
public class Main {

    public static void main(String[] args) {
        ChatMessagesRequest request = new ChatMessagesRequest(0, "", "");
        try {
            HttpResponse response = request.sendRequestForMessages();
            System.out.println(response.getStatusLine());
            Stream.of(response.getAllHeaders()).forEach(System.out::println);
            System.out.println(IOUtils.toString(response.getEntity().getContent()));
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
