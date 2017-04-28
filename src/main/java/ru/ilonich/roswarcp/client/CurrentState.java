package ru.ilonich.roswarcp.client;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Контейнер для хранения текущих установок парсера:
 * 1) Логина и пароля для входа за игрока
 * 2) Кукисов для запроса сообщений из чата залогинненого игрока
 * 3) ID и тип последнего сообщения для продолжения запросов в чат
 */

//should be thread-safe
public final class CurrentState {
    private CurrentState(){
    }

    private final static AtomicReference<String> settedLogin = new AtomicReference<>("none");
    private final static AtomicInteger lastMessageId = new AtomicInteger(0);
    private final static AtomicReference<String> lastMessageType = new AtomicReference<>("");
    private final static AtomicReference<Cookies> cookies = new AtomicReference<>(null);
    private final static AtomicReference<State> status = new AtomicReference<>(State.NO_DATA);

    public enum State{
        NO_DATA("Логин/пароль не установлены"),
        BAD_RESULT("Залогинится не получилось"),
        LOGGED("Логин/пароль установлены, готов к запуску"),
        ON("Чат прослушивается");

        private String description;

        State(String description){
            this.description = description;
        }

        @Override
        public String toString() {
            return this.description;
        }
    }

    public static State getStatus(){
        return status.get();
    }

    static void setStatus(State state){
        status.set(state);
    }

    public static boolean tryStart(){
        return status.compareAndSet(State.LOGGED, State.ON);
    }

    public static boolean tryStop() {
        return status.compareAndSet(State.ON, State.LOGGED);
    }

    public static String getSettedLogin() {
        return settedLogin.get();
    }

    static String setLogin(String login) {
        return settedLogin.getAndSet(login);
    }

    static int getLastMessageId() {
        return lastMessageId.get();
    }

    static boolean setLastMessageId(int oldInt, int newInt) {
        return lastMessageId.compareAndSet(oldInt, newInt);
    }

    static String getLastMessageType() {
        return lastMessageType.get();
    }

    static boolean setLastMessageType(String oldType, String newType) {
        return lastMessageType.compareAndSet(oldType, newType);
    }

    static String getCookiesValue() throws Exception {
        if (cookies.get() !=null) {
            return cookies.get().getCookieString();
        } else {
            throw new Exception("Cookie was not setted");
        }
    }

    static void setCookiesValues(String phpsession, String auth,
                                 String userid, String player,
                                 String playerId) {
        cookies.set(new Cookies(phpsession, auth, userid, player, playerId));
    }

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
            return String.format("PHPSESSID=%s; authkey=%s; userid=%s; player=%s ; player_id=%s", phpSessionId, authKey, userid, player, playerId);
        }
    }

}
