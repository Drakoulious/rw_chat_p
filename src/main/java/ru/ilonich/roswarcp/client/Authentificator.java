package ru.ilonich.roswarcp.client;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    private static final PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
    private static final CloseableHttpClient HTTP_CLIENT_MAIN = HttpClientBuilder.create()
            .setUserAgent(ChatMessagesRequest.USER_AGENT)
            .setDefaultHeaders(HEADERS)
            .setConnectionManager(cm)
            .build();

    public static String authentificate(String login, String password) {
        try {
            CurrentState.skipSettings();
            if (CurrentState.getPhpSessionId() == null){
                CurrentState.setPhpSessionId(getSessionID());
            }
            List<HeaderElement> gettedCookiesList = tryLoginAndGetCookiesList(login, password);
            String authkey = parseAuthkeyValue(gettedCookiesList);
            String userid = parseUserIdValue(gettedCookiesList);
            String player = parsePlayerValue(gettedCookiesList);
            String playerId = parsePlayerIdValue(gettedCookiesList);
            CurrentState.setCookiesValues(authkey, userid, player, playerId);
            CurrentState.setStatus(CurrentState.State.LOGGED);
            CurrentState.setLogin(login);
        } catch (IOException e){
            CurrentState.setStatus(CurrentState.State.BAD_RESULT);
            CurrentState.setLogin(CurrentState.NO_LOGIN);
            return e.getMessage();
        } catch (Exception e){
            CurrentState.setStatus(CurrentState.State.BAD_RESULT);
            CurrentState.setLogin(CurrentState.NO_LOGIN);
            return e.getMessage();
        }
        return "ok";
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

    private static String getSessionID() throws Exception {
        CloseableHttpResponse response = HTTP_CLIENT_MAIN.execute(new HttpGet(ROSWAR_URL));
        if (response.getStatusLine().getStatusCode() != 200){
            throw new Exception("Roswar.ru unavailable");
        }
        HeaderElement[] headerElements = response.getFirstHeader("Set-Cookie") //здесь NPE возможно
                .getElements(); //а тута ParseException или пустой массив;
        response.close();
        return parseSessionID(headerElements);
    }

    /**
     * Возможны внезапные эксепшены см. {@link #getSessionID()} комменты
     */
    private static List<HeaderElement> tryLoginAndGetCookiesList(String login, String password) throws Exception {
        Header[] cookieHeaders = sendPostWithCredentials(login, password);
        return Stream.of(cookieHeaders)
                .flatMap(h -> Stream.of(h.getElements()))
                .collect(Collectors.toList());
    }

    private static Header[] sendPostWithCredentials(String login, String password) throws Exception {
        HttpPost authPost = new HttpPost(ROSWAR_URL);
        authPost.addHeader("Cookie", String.format("PHPSESSID=%s", CurrentState.getPhpSessionId()));
        List<BasicNameValuePair> params = Arrays.asList(LOGIN_ACTION,
                new BasicNameValuePair("email", login),
                new BasicNameValuePair("password", password));
        authPost.setEntity(new UrlEncodedFormEntity(params));
        CloseableHttpResponse response = HTTP_CLIENT_MAIN.execute(authPost); //FIXME after several attempts to login with bad credentials it will stuck without exception (without timeout)
        if (response.getStatusLine().getStatusCode() != 302){
            throw new Exception("Roswar.ru unavailable");
        }
        if ("/login/".equals(response.getFirstHeader("Location").getValue())){
            throw new Exception("Bad credentials");
        }
        Header[] result = response.getHeaders("Set-Cookie");
        response.close();
        return result;

    }

    private static String parseSessionID(HeaderElement... headerElements) throws IOException {
        return Stream.of(headerElements)
                .filter(headerElement -> headerElement.getName().equals("PHPSESSID"))
                .findFirst()
                .orElseThrow(() -> new IOException("No session"))
                .getValue();
    }
}
