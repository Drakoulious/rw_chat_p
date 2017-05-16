package ru.ilonich.roswarcp.model;


import java.sql.Timestamp;

public class CheckedProfile {
    private int playerId;
    private int triggerSysMesId;
    private int lastPrizeDataId;
    private Timestamp lastPrizeDate;
    private Timestamp triggerSysMesDate;

    public CheckedProfile() {
    }

    public CheckedProfile(int playerId, int triggerSysMesId, int lastPrizeDataId, Timestamp lastPrizeDate, Timestamp triggerSysMesDate) {
        this.playerId = playerId;
        this.triggerSysMesId = triggerSysMesId;
        this.lastPrizeDataId = lastPrizeDataId;
        this.lastPrizeDate = lastPrizeDate;
        this.triggerSysMesDate = triggerSysMesDate;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getTriggerSysMesId() {
        return triggerSysMesId;
    }

    public void setTriggerSysMesId(int triggerSysMesId) {
        this.triggerSysMesId = triggerSysMesId;
    }

    public int getLastPrizeDataId() {
        return lastPrizeDataId;
    }

    public void setLastPrizeDataId(int lastPrizeDataId) {
        this.lastPrizeDataId = lastPrizeDataId;
    }

    public Timestamp getLastPrizeDate() {
        return lastPrizeDate;
    }

    public void setLastPrizeDate(Timestamp lastPrizeDate) {
        this.lastPrizeDate = lastPrizeDate;
    }

    @Override
    public String toString() {
        return String.format("CheckedProfile: playerId=%d, triggerSysMesId=%d, lastPrizeDataId=%s, lastPrizeDate=%s, triggerSysMesDate=%s",
                playerId, triggerSysMesId, lastPrizeDataId, lastPrizeDate.toString(), triggerSysMesDate.toString());
    }

    public Timestamp getTriggerSysMesDate() {
        return triggerSysMesDate;
    }

    public void setTriggerSysMesDate(Timestamp triggerSysMesDate) {
        this.triggerSysMesDate = triggerSysMesDate;
    }
}
