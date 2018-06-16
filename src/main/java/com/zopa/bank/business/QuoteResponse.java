package com.zopa.bank.business;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class QuoteResponse implements BankResponse {

    private static final int RATE_SCALE = 1;
    private static final int REPAYMENTS_SCALE = 2;

    private static final DecimalFormat RATE_FORMAT = new DecimalFormat("#0.#");
    private static final DecimalFormat REPAYMENT_FORMAT = new DecimalFormat("#0.##");

    private int requestedAmount;
    private double rate;
    private double monthlyRepayment;
    private double totalRepayment;

    public QuoteResponse(int requestedAmount, double rate, double monthlyRepayment, double totalRepayment) {
        this.requestedAmount = requestedAmount;
        this.rate = rate;
        this.monthlyRepayment = monthlyRepayment;
        this.totalRepayment = totalRepayment;
    }

    @Override
    public String toString() {
        String lineSeparator = System.lineSeparator();
        return String.format("Requested amount: %d %s Rate: %s%% %s Monthly repayment: %s %s Total repayment: %s",
                requestedAmount, lineSeparator, formatDecimal(getRate(), RATE_FORMAT),
                lineSeparator, formatDecimal(getMonthlyRepayment(), REPAYMENT_FORMAT),
                lineSeparator, formatDecimal(getTotalRepayment(), REPAYMENT_FORMAT), lineSeparator);
    }

    private String formatDecimal(double decimal, DecimalFormat decimalFormat) {
        return decimalFormat.format(decimal);
    }

    public int getRequestedAmount() {
        return requestedAmount;
    }

    public double getRate() {
        return new BigDecimal(rate).setScale(RATE_SCALE, RoundingMode.HALF_UP).doubleValue();
    }

    public double getMonthlyRepayment() {
        return new BigDecimal(monthlyRepayment).setScale(REPAYMENTS_SCALE, RoundingMode.HALF_UP).doubleValue();
    }

    public double getTotalRepayment() {
        return new BigDecimal(totalRepayment).setScale(REPAYMENTS_SCALE, RoundingMode.HALF_UP).doubleValue();
    }
}
