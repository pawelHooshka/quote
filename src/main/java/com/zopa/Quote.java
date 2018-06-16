package com.zopa;

import com.zopa.bank.BankManager;
import com.zopa.bank.business.BankResponse;
import com.zopa.bank.business.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Quote {

    private static final Logger logger = LoggerFactory.getLogger("consoleLogger");
    private static final Logger errorLogger = LoggerFactory.getLogger("fileLogger");

    public static void main(String... args) {
        if (args.length != 2) {
            logger.warn("Please try again providing 2 parameters: path to the market file (csv) and the requested amount "
                    + "to borrow");
        } else {
            BankManager bankManager = new BankManager();
            BankResponse bankResponse = bankManager.getQuote(args[0], args[1]);
            handleResponse(bankResponse);
        }
    }

    private static void handleResponse(BankResponse bankResponse) {
        if (bankResponse instanceof ErrorResponse) {
            handleError((ErrorResponse) bankResponse);
        } else {
            logger.info(System.lineSeparator() + " " + bankResponse);
        }
    }

    private static void handleError(ErrorResponse errorResponse) {
        if (errorResponse.getCause() != null) {
            errorLogger.error(System.lineSeparator() + " " + errorResponse, errorResponse.getCause());
        } else {
            logger.warn(System.lineSeparator() + " " + errorResponse);
        }
    }
}
