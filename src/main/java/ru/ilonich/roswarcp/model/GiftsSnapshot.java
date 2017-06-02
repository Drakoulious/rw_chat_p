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
    private int small;
    private int medium;
    private int big;
    private int notOpened;
    private int _small;
    private int _medium;
    private int _big;

    public GiftsSnapshot() {
    }

    public GiftsSnapshot(int giftsQuantity, int lastGiftId, Timestamp time, int idDelta, long timeDelta, int hornDelta, int small, int medium, int big, int notOpened, int small1, int medium1, int big1) {
        this.giftsQuantity = giftsQuantity;
        this.lastGiftId = lastGiftId;
        this.time = time;
        this.idDelta = idDelta;
        this.timeDelta = timeDelta;
        this.hornDelta = hornDelta;
        this.small = small;
        this.medium = medium;
        this.big = big;
        this.notOpened = notOpened;
        this._small = small1;
        this._medium = medium1;
        this._big = big1;
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

    public int getSmall() {
        return small;
    }

    public void setSmall(int small) {
        this.small = small;
    }

    public int getMedium() {
        return medium;
    }

    public void setMedium(int medium) {
        this.medium = medium;
    }

    public int getBig() {
        return big;
    }

    public void setBig(int big) {
        this.big = big;
    }

    public int getNotOpened() {
        return notOpened;
    }

    public void setNotOpened(int notOpened) {
        this.notOpened = notOpened;
    }

    public int get_small() {
        return _small;
    }

    public void set_small(int _small) {
        this._small = _small;
    }

    public int get_medium() {
        return _medium;
    }

    public void set_medium(int _medium) {
        this._medium = _medium;
    }

    public int get_big() {
        return _big;
    }

    public void set_big(int _big) {
        this._big = _big;
    }
}
