package com.zopa.bank;

import com.zopa.TestUtils;
import com.zopa.bank.business.BankResponse;
import com.zopa.bank.business.ErrorResponse;
import com.zopa.bank.business.QuoteResponse;
import com.zopa.data.entity.Lender;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class BankManagerTest {

    private static String CSV_NO_HEADERS_AND_EMPTY_LINES = "csv/dataWithoutEmptyLinesAndHeaders.csv";
    private BankManager bankManager;
    private TestUtils testUtils;

    @Before
    public void init() {
        bankManager = new BankManager();
        testUtils = TestUtils.getInstance();
    }

    @Test
    public void testLendersOrdering() {
        Comparator<Lender> lenderOrdering = bankManager.getLendersOrdering();
        List<Lender> lenders = getLenders();
        Collections.sort(lenders, lenderOrdering);
        verifySortedLenders(lenders);
    }

    @Test
    public void testRequestValidAmount() {
        BankResponse bankResponse = bankManager.getQuote(testUtils.getResourcePath(CSV_NO_HEADERS_AND_EMPTY_LINES), "1000");
        assertThat(bankResponse, instanceOf(QuoteResponse.class));
    }

    @Test
    public void testRequestAmountTooLow() {
        BankResponse bankResponse = bankManager.getQuote(testUtils.getResourcePath(CSV_NO_HEADERS_AND_EMPTY_LINES), "50");
        assertErrorResponseWithoutException(bankResponse);
    }

    @Test
    public void testRequestNegativeAmount() {
        BankResponse bankResponse = bankManager.getQuote(testUtils.getResourcePath(CSV_NO_HEADERS_AND_EMPTY_LINES), "-50");
        assertErrorResponseWithoutException(bankResponse);
    }

    @Test
    public void testRequestAmountTooHigh() {
        BankResponse bankResponse = bankManager.getQuote(testUtils.getResourcePath(CSV_NO_HEADERS_AND_EMPTY_LINES), "20000");
        assertErrorResponseWithoutException(bankResponse);
    }

    @Test
    public void testRequestAmountNotIncrementOfHundred() {
        BankResponse bankResponse = bankManager.getQuote(testUtils.getResourcePath(CSV_NO_HEADERS_AND_EMPTY_LINES), "1050");
        assertErrorResponseWithoutException(bankResponse);
    }

    @Test
    public void testRequestNotParsableAmount() {
        BankResponse bankResponse = bankManager.getQuote(testUtils.getResourcePath(CSV_NO_HEADERS_AND_EMPTY_LINES), "abc");
        assertErrorResponseWithException(bankResponse);
    }

    private void assertErrorResponseWithoutException(BankResponse bankResponse) {
        assertThat(bankResponse, instanceOf(ErrorResponse.class));
        ErrorResponse errorResponse = (ErrorResponse) bankResponse;
        assertThat(errorResponse.toString(),
                is("Please try again - we can only accept amounts in range from £1000 and £15000 in £100 increments only."));
        assertNull(errorResponse.getCause());
    }

    private void assertErrorResponseWithException(BankResponse bankResponse) {
        assertThat(bankResponse, instanceOf(ErrorResponse.class));
        ErrorResponse errorResponse = (ErrorResponse) bankResponse;
        assertThat(errorResponse.toString(), is("An error has occurred!"));
        assertNotNull(errorResponse.getCause());
        assertThat(errorResponse.getCause(), instanceOf(NumberFormatException.class));
    }

    private static List<Lender> getLenders() {
        List<Lender> lenders = new ArrayList<>();
        lenders.add(new Lender("Bob",0.075,640));
        lenders.add(new Lender("Jane",0.069,480));
        lenders.add(new Lender("Fred",0.071,520));
        lenders.add(new Lender("Mary",0.104,170));
        lenders.add(new Lender("John",0.081,320));
        lenders.add(new Lender("Dave",0.074,140));
        lenders.add(new Lender("Angela",0.071,60));

        return lenders;
    }

    private void verifySortedLenders(List<Lender> sortedLenders) {
        assertThat(sortedLenders.get(0).getName(), is("Jane"));
        assertThat(sortedLenders.get(1).getName(), is("Fred"));
        assertThat(sortedLenders.get(2).getName(), is("Angela"));
        assertThat(sortedLenders.get(3).getName(), is("Dave"));
        assertThat(sortedLenders.get(4).getName(), is("Bob"));
        assertThat(sortedLenders.get(5).getName(), is("John"));
        assertThat(sortedLenders.get(6).getName(), is("Mary"));
    }
}
