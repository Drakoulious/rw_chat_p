package ru.ilonich.roswarcp.client;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Никола on 13.01.2017.
 */
public class ChatMessagesRequest {
    private static final String CHAT_URL = "http://www.roswar.ru/chat/get-messages/";
    static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36";
    private static final List<BasicHeader> HEADERS = Arrays.asList(
            new BasicHeader("Accept", "application/json, text/javascript, */*; q=0.01"),
            new BasicHeader("Accept-Encoding", "gzip, deflate"),
            new BasicHeader("Accept-Language", "ru-RU,ru;q=0.8,en-US;q=0.6,en;q=0.4"),
            new BasicHeader("Connection", "keep-alive"),
            new BasicHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8"),
            new BasicHeader("Host", "www.roswar.ru"),
            new BasicHeader("Origin", "http://www.roswar.ru"),
            new BasicHeader("Referer", "http://www.roswar.ru/chat/chat/"),
            new BasicHeader("X-Requested-With", "XMLHttpRequest")
    );
    private static final HttpClient HTTP_CLIENT_CHAT = HttpClientBuilder.create().setUserAgent(USER_AGENT)
            .setDefaultHeaders(HEADERS).build();
    private static final ReentrantLock CLIENT_LOCK = new ReentrantLock();

    private int lastMessageId;
    private String type;
    private String cookieValue;

    ChatMessagesRequest(int lastMessageId, String type, String cookieValue){
        this.lastMessageId = lastMessageId;
        this.type = type;
        this.cookieValue = cookieValue;
    }

    HttpResponse requestMessages() {
        HttpPost httpPost = new HttpPost(CHAT_URL);
        List<BasicNameValuePair> params = Arrays.asList(
                new BasicNameValuePair("lastMessageId", String.valueOf(lastMessageId)),
                new BasicNameValuePair("type", type));
        httpPost.setHeader("Cookie", cookieValue);
        HttpResponse response = null;
        try {
            CLIENT_LOCK.lock();
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            response = HTTP_CLIENT_CHAT.execute(httpPost);
        } catch (IOException e) {
            //log
            System.out.println(e.getMessage());
            CurrentState.setStatus(CurrentState.State.BAD_RESULT);
        } finally {
            CLIENT_LOCK.unlock();
        }
        return response;
    }

    public int getLastMessageId() {
        return lastMessageId;
    }

    public String getType() {
        return type;
    }
}
