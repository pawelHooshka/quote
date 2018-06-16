package com.zopa.bank;

import com.zopa.bank.business.BankQuotingEngine;
import com.zopa.bank.business.BankResponse;
import com.zopa.bank.business.ErrorResponse;
import com.zopa.data.CsvDataSource;
import com.zopa.data.entity.Lender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BankManager {

    private final static Logger logger = LoggerFactory.getLogger("consoleLogger");
    private final Comparator<Lender> lendersOrdering;

    private final int MIN_AMOUNT = 1000;
    private final int MAX_AMOUNT = 15000;
    private final int ACCEPTABLE_INTERVAL = 100;

    public BankManager() {
        this.lendersOrdering = Comparator.comparingDouble(Lender::getRate);
    }

    Comparator<Lender> getLendersOrdering() {
        return lendersOrdering;
    }

    public BankResponse getQuote(String marketCsv, String requested) {
        CsvDataSource csvDataSource = new CsvDataSource(marketCsv);
        try {
            Integer requestedAmount = getAmount(requested);
            if (requestedAmount == null) {
                return new ErrorResponse("Please try again - we can only accept amounts in range from " +
                        "£1000 and £15000 in £100 increments only.");
            }

            List<Lender> lenders = csvDataSource.getLenders();
            Collections.sort(lenders, lendersOrdering);

            return BankQuotingEngine.getQuote(lenders, requestedAmount);
        } catch (Exception e) {
            logger.error("An error '{}' has occurred while processing your request - please try again with correct parameters. " +
                            "Please inspect the log file 'quote.log' in the current directory for more details.",
                    e.getClass().getName());
            return new ErrorResponse("An error has occurred!", e);
        }
    }

    private Integer getAmount(String request) {
        Integer requestedAmount = Integer.parseInt(request);
        if (requestedAmount % ACCEPTABLE_INTERVAL != 0) {
            return null;
        } else if (requestedAmount < MIN_AMOUNT || requestedAmount > MAX_AMOUNT) {
            return null;
        } else {
            return requestedAmount;
        }
    }
}
