package com.zopa.bank.business;

import com.zopa.data.entity.Lender;

import java.util.ArrayList;
import java.util.List;

public class BankQuotingEngine {

    private static final int LOAN_LENGTH_IN_YEARS = 3;

    public static BankResponse getQuote(List<Lender> lenders, int requestedAmount) {
        double fundsAvailable = getFunds(lenders);
        if (Double.compare(fundsAvailable, requestedAmount) <= 0) {
            return new ErrorResponse("It is not possible to provide a quote to satisfy the requested amount %d",
                    requestedAmount);
        }

        List<Lender> creditors = getMyCreditors(lenders, requestedAmount);
        double creditorsFunds = getFunds(creditors);
        return getQuote(creditors, creditorsFunds - requestedAmount, requestedAmount);
    }

    private static List<Lender> getMyCreditors(List<Lender> lenders, int requestedAmount) {
        List<Lender> creditors = new ArrayList<>();
        double funds = 0;

        for (Lender lender : lenders) {
            if (Double.compare(funds, requestedAmount) < 0) {
                funds += lender.getFunds();
                creditors.add(lender);
            } else {
                return creditors;
            }
        }

        return creditors;
    }

    private static QuoteResponse getQuote(List<Lender> creditors, double overlapValue, int requestedAmount) {
        double totalRepayment = 0;
        for (int i = 0; i < creditors.size(); i++) {
            Lender creditor = creditors.get(i);

            double total = getCompoundedAmount(debitCreditor(creditor, i, creditors.size() - 1, overlapValue),
                    creditor.getRate());
            totalRepayment += total;
        }

        double monthlyRepayment = totalRepayment / (LOAN_LENGTH_IN_YEARS * 12);
        double rate = (Math.pow(totalRepayment / (double)requestedAmount, 1d / (double)LOAN_LENGTH_IN_YEARS) - 1) * 100.0;
        return new QuoteResponse(requestedAmount, rate, monthlyRepayment, totalRepayment);
    }

    private static double debitCreditor(Lender creditor, int currentLenderRank, int maxLenderRank,
        double amountOverlap) {

        if (currentLenderRank == maxLenderRank) {
            return creditor.getFunds() - amountOverlap;
        } else {
            return creditor.getFunds();
        }
    }

    private static double getFunds(List<Lender> lenders) {
        return lenders.stream().mapToDouble(lender -> lender.getFunds()).sum();
    }

    private static double getCompoundedAmount(double startingAmount, double rate) {
        return startingAmount * Math.pow(1 + rate, LOAN_LENGTH_IN_YEARS);
    }
}
