package com.example.ncproject.DAO.Utils.ModelsUtils;

public class Addition {
    private int from;
    private int till;
    private double price;
    private boolean isPerHour;

    public int getFrom() {
        return from;
    }

    public int getTill() {
        return till;
    }

    public double getPrice() {
        return price;
    }

    public boolean isPerHour() {
        return isPerHour;
    }

    @Override
    public String toString(){
        return "{ "+getFrom()+", "+getTill()+", "+getPrice()+", "+isPerHour()+"}";
    }

    public Addition(int from, int till, double price, boolean isPerHour) {
        this.from = from;
        this.till = till;
        this.price = price;
        this.isPerHour = isPerHour;
    }
    public Addition(int from, int till, double price) {
        this.from = from;
        this.till = till;
        this.price = price;
        this.isPerHour = false;
    }
}
