package com.example.parkapp.Bean;

import java.util.List;

public class PredictBean {

    /**
     * name : 文心二路
     * capacity : 15
     * occupy : 5
     * remain : 10
     * atitude : 118,119
     * predict : [12,14,12,11,13,11,11,11,11,11,11,11]
     */

    private String name;
    private int capacity;
    private int occupy;
    private int remain;
    private String atitude;
    private List<Integer> predict;

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

    public List<Integer> getPredict() {
        return predict;
    }

    public void setPredict(List<Integer> predict) {
        this.predict = predict;
    }
}
