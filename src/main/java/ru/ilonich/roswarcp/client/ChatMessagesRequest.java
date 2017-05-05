package ru.ilonich.roswarcp.client;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

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

    static final PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
    private static final CloseableHttpClient HTTP_CLIENT_CHAT = HttpClientBuilder.create().setUserAgent(USER_AGENT)
            .setDefaultHeaders(HEADERS).setConnectionManager(cm).build();

    private int lastMessageId;
    private String type;
    private String cookieValue;

    ChatMessagesRequest(int lastMessageId, String type, String cookieValue){
        this.lastMessageId = lastMessageId;
        this.type = type;
        this.cookieValue = cookieValue;
    }

    String requestMessages() {
        HttpPost httpPost = new HttpPost(CHAT_URL);
        List<BasicNameValuePair> params = Arrays.asList(
                new BasicNameValuePair("lastMessageId", String.valueOf(lastMessageId)),
                new BasicNameValuePair("type", type));
        httpPost.setHeader("Cookie", cookieValue);
        String result = null;
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            CloseableHttpResponse response = HTTP_CLIENT_CHAT.execute(httpPost);
            result = getJson(response);
            response.close();
        } catch (IOException e) {
            //log
            System.out.println(e.getMessage());
            CurrentState.setStatus(CurrentState.State.BAD_RESULT);
        }
        return result;
    }

    public int getLastMessageId() {
        return lastMessageId;
    }

    public String getType() {
        return type;
    }

    private static String getJson(HttpResponse response) throws IOException {
        return IOUtils.toString(response.getEntity().getContent(), Charset.defaultCharset());
    }
}
