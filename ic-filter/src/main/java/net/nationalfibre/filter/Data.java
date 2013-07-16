package net.nationalfibre.filter;

import java.util.Date;

public class Data {

    private String hash;
    private Long timestamp;

    public Data(String hash, Date date) {
        this.hash       = hash;
        this.timestamp  = (date.getTime() / 1000);
    }

    public Data(String hash, Long timestamp) {
        this.hash       = hash;
        this.timestamp  = timestamp;
    }

    public String getHash() {
        return hash;
    }

    public Long getTimestamp() {
        return timestamp;
    }
}
