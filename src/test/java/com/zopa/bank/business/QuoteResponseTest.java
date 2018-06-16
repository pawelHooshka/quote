package com.zopa.bank.business;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class QuoteResponseTest {

    @Test
    public void testQuoteResponse() {
        QuoteResponse quoteResponse = new QuoteResponse(1000, 6.899999999999, 29.88899999, 1200.56666647);
        assertThat(quoteResponse.getRequestedAmount(), is(1000));
        assertThat(quoteResponse.getRate(), is(6.9));
        assertThat(quoteResponse.getMonthlyRepayment(), is(29.89));
        assertThat(quoteResponse.getTotalRepayment(), is(1200.57));
    }
}
