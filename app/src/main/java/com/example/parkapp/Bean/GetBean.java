package com.example.parkapp.Bean;

public class GetBean {

    /**
     * name : 文心二路
     * type : short
     * state : 未完成
     * destination : 文心三路
     * use : 2.5
     * price_info : 15元每小时，超时xxxx，即收费说明
     * price : 40
     */

    private String name;
    private String type;
    private String state;
    private String destination;
    private double use;
    private String price_info;
    private int price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public double getUse() {
        return use;
    }

    public void setUse(double use) {
        this.use = use;
    }

    public String getPrice_info() {
        return price_info;
    }

    public void setPrice_info(String price_info) {
        this.price_info = price_info;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
