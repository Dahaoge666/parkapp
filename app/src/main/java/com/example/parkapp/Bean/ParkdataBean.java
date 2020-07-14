package com.example.parkapp.Bean;

public class ParkdataBean {

    /**
     * name : 文心二路
     * capacity : 15
     * occupy : 5
     * remain : 10
     * atitude : 118,119
     */

    private String name;
    private int capacity;
    private int occupy;
    private int remain;
    private String atitude;

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
}
