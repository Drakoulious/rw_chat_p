package ru.ilonich.roswarcp.client;

import org.apache.http.HeaderElement;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Никола on 13.01.2017.
 */
public final class Authentificator {

    private static final String ROSWAR_URL = "http://www.roswar.ru";

    private static final BasicNameValuePair LOGIN_ACTION = new BasicNameValuePair("action", "login");

    private static final List<BasicHeader> HEADERS = Arrays.asList(
            new BasicHeader("Connection", "keep-alive"),
            new BasicHeader("Upgrade-Insecure-Requests", "1"),
            new BasicHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*//*;q=0.8"),
            new BasicHeader("Accept-Encoding", "gzip, deflate, sdch"),
            new BasicHeader("Accept-Language", "ru-RU,ru;q=0.8,en-US;q=0.6,en;q=0.4"),
            new BasicHeader("If-Modified-Since", ZonedDateTime.now().format(DateTimeFormatter.ofPattern("EEE, d MMM uuuu HH:mm:ss 'GMT'", Locale.ENGLISH))));

    private static final HttpClient HTTP_CLIENT_MAIN = HttpClientBuilder.create()
            .setUserAgent(ChatMessagesRequest.USER_AGENT)
            .setDefaultHeaders(HEADERS)
            .build();

    private static final ReentrantLock CLIENT_LOCK = new ReentrantLock();

    public static boolean authentificate(String login, String password) {
        try {
            String phpsessid = getSessionID();
            List<HeaderElement> gettedCookiesList = tryLoginAndGetCookiesList(login, password, phpsessid);
            String authkey = parseAuthkeyValue(gettedCookiesList);
            String userid = parseUserIdValue(gettedCookiesList);
            String player = parsePlayerValue(gettedCookiesList);
            String playerId = parsePlayerIdValue(gettedCookiesList);
            CurrentState.setCookiesValues(phpsessid, authkey, userid, player, playerId);
            CurrentState.setStatus(CurrentState.State.LOGGED);
            CurrentState.setLogin(login);
        } catch (IOException e){
            //log
            System.out.println(e.getMessage());
            CurrentState.setStatus(CurrentState.State.BAD_RESULT);
            return false;
        } catch (Exception e){
            //wtf
            System.out.println(e.getMessage() + "===Unexpected===");
            CurrentState.setStatus(CurrentState.State.BAD_RESULT);
            return false;
        }
        return true;
    }

    private static String parsePlayerIdValue(List<HeaderElement> headerElementList) throws IOException {
        return headerElementList.stream()
                .filter(a -> a.getName().equals("player_id"))
                .findFirst()
                .orElseThrow(() -> new IOException("player ID not found"))
                .getValue();
    }

    private static String parsePlayerValue(List<HeaderElement> headerElementList) throws IOException {
        return headerElementList.stream()
                .filter(a -> a.getName().equals("player"))
                .findFirst()
                .orElseThrow(() -> new IOException("player not found"))
                .getValue();
    }

    private static String parseUserIdValue(List<HeaderElement> headerElementList) throws IOException {
        return headerElementList.stream()
                .filter(a -> a.getName().equals("userid") && !a.getValue().equals("deleted"))
                .findFirst()
                .orElseThrow(() -> new IOException("userid not found"))
                .getValue();
    }

    private static String parseAuthkeyValue(List<HeaderElement> headerElementList) throws IOException {
        return headerElementList.stream()
                .filter(a -> a.getName().equals("authkey") && !a.getValue().equals("deleted"))
                .findFirst()
                .orElseThrow(() -> new IOException("authkey not found"))
                .getValue();
    }

    /**
     * Возможные эксепшены см. {@link #getSessionID()} комменты
     */
    private static List<HeaderElement> tryLoginAndGetCookiesList(String login, String password, String sessionId) throws IOException {
        HttpResponse loginAttemptResponse = sendPostWithCredentials(login, password, sessionId);
        return Stream.of(loginAttemptResponse.getHeaders("Set-Cookie"))
                .flatMap(h -> Stream.of(h.getElements()))
                .collect(Collectors.toList());
    }

    private static String getSessionID() throws IOException {
        try {
            CLIENT_LOCK.lock();
            HeaderElement[] headerElements = HTTP_CLIENT_MAIN.execute(new HttpGet(ROSWAR_URL))
                    .getFirstHeader("Set-Cookie") //здесь NPE возможно
                    .getElements(); //а тута ParseException или пустой массив;
            return parseSessionID(headerElements);
        } finally {
            CLIENT_LOCK.unlock();
        }
    }

    private static HttpResponse sendPostWithCredentials(String login, String password, String sessionId) throws IOException {
        HttpPost authPost = new HttpPost(ROSWAR_URL);
        authPost.addHeader("Cookie", String.format("PHPSESSID=%s", sessionId));
        List<BasicNameValuePair> params = Arrays.asList(LOGIN_ACTION,
                new BasicNameValuePair("email", login),
                new BasicNameValuePair("password", password));
        authPost.setEntity(new UrlEncodedFormEntity(params));
        try {
            CLIENT_LOCK.lock();
            return HTTP_CLIENT_MAIN.execute(authPost);
        } finally {
            CLIENT_LOCK.unlock();
        }
    }

    private static String parseSessionID(HeaderElement... headerElements) throws IOException {
        return Stream.of(headerElements)
                .filter(headerElement -> headerElement.getName().equals("PHPSESSID"))
                .findFirst()
                .orElseThrow(() -> new IOException("No session"))
                .getValue();
    }
}
