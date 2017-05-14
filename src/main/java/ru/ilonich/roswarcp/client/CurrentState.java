package ru.ilonich.roswarcp.client;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public final class CurrentState {
    final static String NO_LOGIN = "none";
    private CurrentState(){
    }

    private final static AtomicReference<String> settedLogin = new AtomicReference<>(NO_LOGIN);
    private final static AtomicInteger lastMessageId = new AtomicInteger(0);
    private final static AtomicReference<String> lastMessageType = new AtomicReference<>("");
    private final static AtomicReference<String> phpSessionId = new AtomicReference<>(null);
    private final static AtomicReference<Cookies> cookies = new AtomicReference<>(null);
    private final static AtomicReference<State> status = new AtomicReference<>(State.NO_DATA);

    static void setPhpSessionId(String sid) {
        phpSessionId.set(sid);
    }

    static String getPhpSessionId() {
        return phpSessionId.get();
    }

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

    static void skipSettings(){
        status.set(State.NO_DATA);
        settedLogin.set(NO_LOGIN);
        lastMessageId.set(0);
        lastMessageType.set("");
        cookies.set(null);
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

    static void setCookiesValues(String auth,
                                 String userid, String player,
                                 String playerId) {
        cookies.set(new Cookies(getPhpSessionId(), auth, userid, player, playerId));
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
