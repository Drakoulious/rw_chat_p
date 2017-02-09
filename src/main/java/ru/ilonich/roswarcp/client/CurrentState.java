package ru.ilonich.roswarcp.client;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Никола on 19.01.2017.
 */
//Контейнер для хранения текущих установок парсера:
// 1) Логина и пароля для входа за игрока
// 2) Кукисов для запроса сообщений из чата залогинненого игрока
// 3) ID и тип последнего сообщения для продолжения запросов в чат
//should be thread-safe
public final class CurrentState {
    private CurrentState(){
    }

    private static AtomicReference<String> settedLogin = new AtomicReference<>("none");
    private static AtomicReference<String> settedPassword = new AtomicReference<>("");
    private static AtomicBoolean isLogged = new AtomicBoolean(false);
    private static AtomicInteger lastMessageId = new AtomicInteger(0);
    private static AtomicReference<String> lastMessageType = new AtomicReference<>("");
    private static AtomicReference<Cookies> cookies = new AtomicReference<>(null);


    private static class Cookies {
        String phpSessionId;
        String authKey;
        String userid;
        String player;
        String playerId;

        Cookies(String phpSessionId, String authKey, String userid, String player, String playerId) {
            this.phpSessionId = phpSessionId;
            this.authKey = authKey;
            this.userid = userid;
            this.player = player;
            this.playerId = playerId;
        }

        String getCookieString(){
            return String.format("a=%s; b=%s; etc...", phpSessionId, authKey);
        }
    }

}
