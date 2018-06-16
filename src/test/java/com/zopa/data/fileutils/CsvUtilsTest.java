package com.zopa.data.fileutils;

import com.zopa.data.entity.Lender;
import com.zopa.data.exceptions.IncompatibleCsvLine;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

public class CsvUtilsTest {

    @Test
    public void testGetLender() throws IncompatibleCsvLine {
        Lender lender = CsvUtils.getLender("Bob,0.075,640");
        assertNotNull(lender);
        assertThat(lender.getName(), is("Bob"));
        assertThat(lender.getRate(), is(0.075));
        assertThat(lender.getFunds(), is(640.0));
    }

    @Test
    public void testGetLenderIsNullAndNoException() {
        try {
            Lender lender = CsvUtils.getLender("");
            assertNull(lender);

            lender = CsvUtils.getLender(null);
            assertNull(lender);
        } catch (Exception e) {
            fail("Exception is not expected here");
        }
    }

    @Test
    public void testNonsenseLine() {
        try {
            CsvUtils.getLender("abc");
        } catch (Exception e) {
            assertThat(e, instanceOf(IncompatibleCsvLine.class));
            assertThat(e.getMessage(), is("incomplete CSV line 'abc'"));
        }
    }

    @Test
    public void testEmptyValuesLine() {
        try {
            CsvUtils.getLender(",,,");
        } catch (Exception e) {
            assertThat(e, instanceOf(IncompatibleCsvLine.class));
            assertThat(e.getMessage(), is("incomplete CSV line ',,,'"));
        }
    }

    @Test
    public void testIncompleteLineLine() {
        try {
            CsvUtils.getLender("Bob,0.075");
        } catch (Exception e) {
            assertThat(e, instanceOf(IncompatibleCsvLine.class));
            assertThat(e.getMessage(), is("incomplete CSV line 'Bob,0.075'"));
        }
    }

    @Test
    public void testIncompleteLineLineWithEmptyValue() {
        try {
            CsvUtils.getLender("Bob,0.075,");
        } catch (Exception e) {
            assertThat(e, instanceOf(IncompatibleCsvLine.class));
            assertThat(e.getMessage(), is("incomplete CSV line 'Bob,0.075,'"));
        }
    }

    @Test
    public void testEmptyUnparsableDoublesLine() {
        try {
            CsvUtils.getLender("Lender,Rate,Available");
        } catch (Exception e) {
            assertThat(e, instanceOf(NumberFormatException.class));
            assertThat(e.getMessage(), is("For input string: \"Rate\""));
        }
    }
}
