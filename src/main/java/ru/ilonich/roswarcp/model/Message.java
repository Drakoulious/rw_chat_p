package ru.ilonich.roswarcp.model;

import java.sql.Timestamp;

/**
 * Created by Илоныч on 14.04.2017.
 */
public class Message {
    public Message(int id, int playerId, Timestamp time, int roomId, String type, int channel, String text, int linkedPlayerId, String nickName, String fraction, int level, String clanStatus, int clanId, int flags, String clanName) {
        this.id = id;
        this.playerId = playerId;
        this.time = time;
        this.roomId = roomId;
        this.type = type;
        this.channel = channel;
        this.text = text;
        this.linkedPlayerId = linkedPlayerId;
        this.nickName = nickName;
        this.fraction = fraction;
        this.level = level;
        this.clanStatus = clanStatus;
        this.clanId = clanId;
        this.flags = flags;
        this.clanName = clanName;
    }

    public Message() {
    }
    //тело
    private int id;
    private int playerId;
    private Timestamp time;
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

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", playerId=" + playerId +
                ", time=" + time +
                ", roomId=" + roomId +
                ", type='" + type + '\'' +
                ", channel=" + channel +
                ", text='" + text + '\'' +
                ", linkedPlayerId=" + linkedPlayerId +
                ", nickName='" + nickName + '\'' +
                ", fraction='" + fraction + '\'' +
                ", level=" + level +
                ", clanStatus='" + clanStatus + '\'' +
                ", clanId=" + clanId +
                ", flags=" + flags +
                ", clanName='" + clanName + '\'' +
                '}';
    }
}
