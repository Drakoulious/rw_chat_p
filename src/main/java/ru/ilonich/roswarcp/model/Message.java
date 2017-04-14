package ru.ilonich.roswarcp.model;

/**
 * Created by Илоныч on 14.04.2017.
 */
public class Message {
    public Message() {
    }
    //тело
    private int id;
    private int playerId;
    private long time;
    private int roomId;
    private String type;
    private int channel;
    private String text;
    private int linkedPlayerId;
    //игрок
    private String nickName;
    private String fraction;
    private int level;
    private String clanStatus;
    private int clanId;
    private int flags;
    private String clanName;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getClanStatus() {
        return clanStatus;
    }

    public void setClanStatus(String clanStatus) {
        this.clanStatus = clanStatus;
    }

    public int getClanId() {
        return clanId;
    }

    public void setClanId(int clanId) {
        this.clanId = clanId;
    }

    public int getFlags() {
        return flags;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public String getClanName() {
        return clanName;
    }

    public void setClanName(String clanName) {
        this.clanName = clanName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public String getFraction() {
        return fraction;
    }

    public void setFraction(String fraction) {
        this.fraction = fraction;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getLinkedPlayerId() {
        return linkedPlayerId;
    }

    public void setLinkedPlayerId(int linkedPlayerId) {
        this.linkedPlayerId = linkedPlayerId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }
}
