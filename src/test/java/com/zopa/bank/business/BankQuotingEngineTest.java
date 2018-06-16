package com.zopa.bank.business;

import com.zopa.data.entity.Lender;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class BankQuotingEngineTest {

    @Test
    public void testGetQuote() {
        List<Lender> lenders = getLenders();
        BankResponse bankResponse = BankQuotingEngine.getQuote(lenders, 1000);
        assertNotNull(bankResponse);
        assertThat(bankResponse, instanceOf(QuoteResponse.class));
        QuoteResponse response = (QuoteResponse)bankResponse;
        assertThat(response.getRequestedAmount(), is(1000));
        assertThat(response.getRate(), is(7.0));
        assertThat(response.getTotalRepayment(), is(1225.18));
        assertThat(response.getMonthlyRepayment(), is(34.03));
    }

    @Test
    public void testGetQuoteAmountNotAvailable() {
        List<Lender> lenders = getLenders();
        BankResponse bankResponse = BankQuotingEngine.getQuote(lenders, 10000);
        assertNotNull(bankResponse);
        assertThat(bankResponse, instanceOf(ErrorResponse.class));
        ErrorResponse response = (ErrorResponse) bankResponse;
        assertThat(response.toString(), is("It is not possible to provide a quote to satisfy the requested amount 10000"));
        assertNull(response.getCause());
    }

    private static List<Lender> getLenders() {
        List<Lender> lenders = new ArrayList<>();
        lenders.add(new Lender("Jane",0.069,480));
        lenders.add(new Lender("Fred",0.071,520));
        lenders.add(new Lender("Angela",0.071,60));
        lenders.add(new Lender("Dave",0.074,140));
        lenders.add(new Lender("Bob",0.075,640));
        lenders.add(new Lender("John",0.081,320));
        lenders.add(new Lender("Mary",0.104,170));

        return lenders;
    }
}
