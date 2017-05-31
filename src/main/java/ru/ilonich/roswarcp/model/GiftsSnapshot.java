package ru.ilonich.roswarcp.model;

import java.sql.Timestamp;

/**
 * Created by Никола on 30.05.2017.
 */
public class GiftsSnapshot {
    private int giftsQuantity;
    private int lastGiftId;
    private Timestamp time;
    private int idDelta;
    private long timeDelta;
    private int hornDelta;

    public GiftsSnapshot() {
    }

    public GiftsSnapshot(int giftsQuantity, int lastGiftId, Timestamp time, int idDelta, long timeDelta, int hornDelta) {
        this.giftsQuantity = giftsQuantity;
        this.lastGiftId = lastGiftId;
        this.time = time;
        this.idDelta = idDelta;
        this.timeDelta = timeDelta;
        this.hornDelta = hornDelta;
    }

    public int getGiftsQuantity() {
        return giftsQuantity;
    }

    public void setGiftsQuantity(int giftsQuantity) {
        this.giftsQuantity = giftsQuantity;
    }

    public int getLastGiftId() {
        return lastGiftId;
    }

    public void setLastGiftId(int lastGiftId) {
        this.lastGiftId = lastGiftId;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public int getIdDelta() {
        return idDelta;
    }

    public void setIdDelta(int idDelta) {
        this.idDelta = idDelta;
    }

    public long getTimeDelta() {
        return timeDelta;
    }

    public void setTimeDelta(long timeDelta) {
        this.timeDelta = timeDelta;
    }

    @Override
    public String toString() {
        return "GiftsSnapshot{" +
                "giftsQuantity=" + giftsQuantity +
                ", lastGiftId=" + lastGiftId +
                ", time=" + time +
                ", idDelta=" + idDelta +
                ", timeDelta=" + timeDelta +
                '}';
    }

    public int getHornDelta() {
        return hornDelta;
    }

    public void setHornDelta(int hornDelta) {
        this.hornDelta = hornDelta;
    }
}
