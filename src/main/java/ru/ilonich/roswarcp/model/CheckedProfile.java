package ru.ilonich.roswarcp.model;


import java.sql.Timestamp;

public class CheckedProfile {
    private int playerId;
    private int triggerSysMesId;
    private int lastPrizeDataId;
    private Timestamp lastPrizeDate;

    public CheckedProfile() {
    }

    public CheckedProfile(int playerId, int triggerSysMesId, int lastPrizeDataId, Timestamp lastPrizeDate) {
        this.playerId = playerId;
        this.triggerSysMesId = triggerSysMesId;
        this.lastPrizeDataId = lastPrizeDataId;
        this.lastPrizeDate = lastPrizeDate;
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
        return String.format("CheckedProfile: playerId=%d, triggerSysMesId=%d, lastPrizeDataId=%s, lastPrizeDate=%s",
                playerId, triggerSysMesId, lastPrizeDataId, lastPrizeDate.toString());
    }
}
