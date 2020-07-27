package com.example.parkapp.Bean;

public class ReserveBean {

    /**
     * name : 文心二路
     * capacity : 15
     * occupy : 5
     * remain : 10
     * atitude : 118,119
     * price_info : 15元每小时，超时xxxx，即收费说明
     * time_use : 5小时20分钟
     * distance : 2800
     */

    private String name;
    private int capacity;
    private int occupy;
    private int remain;
    private String atitude;
    private String price_info;
    private String time_use;
    private Double distance;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getOccupy() {
        return occupy;
    }

    public void setOccupy(int occupy) {
        this.occupy = occupy;
    }

    public int getRemain() {
        return remain;
    }

    public void setRemain(int remain) {
        this.remain = remain;
    }

    public String getAtitude() {
        return atitude;
    }

    public void setAtitude(String atitude) {
        this.atitude = atitude;
    }

    public String getPrice_info() {
        return price_info;
    }

    public void setPrice_info(String price_info) {
        this.price_info = price_info;
    }

    public String getTime_use() {
        return time_use;
    }

    public void setTime_use(String time_use) {
        this.time_use = time_use;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }
}
