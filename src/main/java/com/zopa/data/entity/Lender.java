package com.zopa.data.entity;

public class Lender {

    private final String name;
    private final double rate;
    private final double funds;

    public Lender(String name, double rate, double funds) {
        this.name = name;
        this.rate = rate;
        this.funds = funds;
    }

    public String getName() {
        return name;
    }

    public double getRate() {
        return rate;
    }

    public double getFunds() {
        return funds;
    }
}
