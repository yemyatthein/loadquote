package com.yemyat.zopa.challenge.model;

public final class LenderInfo {
    private double rate;
    private int amount;

    public double getRate() {
        return rate;
    }

    public int getAmount() {
        return amount;
    }

    public LenderInfo(double rate, int amount) {
        this.rate = rate;
        this.amount = amount;
    }
}
