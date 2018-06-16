package com.zopa.data;

import com.zopa.TestUtils;
import com.zopa.data.entity.Lender;
import com.zopa.data.exceptions.IncompatibleCsvLine;
import com.zopa.data.exceptions.MalformedLineException;
import com.zopa.data.fileutils.CsvUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyString;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({CsvUtils.class})
public class CsvDataSourceTest {

    private static String CSV_NO_HEADERS_AND_EMPTY_LINES = "csv/dataWithoutEmptyLinesAndHeaders.csv";
    private static String CSV_WITHOUT_HEADERS_BUT_WITH_EMPTY_LINES = "csv/dataWithoutHeaders.csv";
    private static String CSV_WITHOUT_EMPTY_LINES_BUT_WITH_HEADERS = "csv/dataWithoutEmptyLines.csv";
    private static String CSV_WITH_HEADERS_AND_EMPTY_LINES = "csv/dataWithHeaders.csv";
    private static String CSV_MALFORMED_LINE_3 = "csv/dataWithHeadersAndMalformedLineOnLine3.csv";

    private TestUtils testUtils;

    @Before
    public void init() {
        testUtils = TestUtils.getInstance();
    }

    @Test
    public void testParseLendersWithoutHeadersAndEmptyLines() {
        CsvDataSource csvDataSource = new CsvDataSource(testUtils.getResourcePath(CSV_NO_HEADERS_AND_EMPTY_LINES));
        try {
            List<Lender> lenders = csvDataSource.getLenders();
            assertLenders(lenders);
        } catch (Exception e) {
            fail("Exception is not expected here");
        }
    }

    @Test
    public void testParseLendersWithoutHeadersButWithEmptyLines() {
        CsvDataSource csvDataSource = new CsvDataSource(testUtils.getResourcePath(CSV_WITHOUT_HEADERS_BUT_WITH_EMPTY_LINES));
        try {
            List<Lender> lenders = csvDataSource.getLenders();
            assertLenders(lenders);
        } catch (Exception e) {
            fail("Exception is not expected here");
        }
    }

    @Test
    public void testParseLendersWithoutEmptyLinesButWithHeaders() {
        CsvDataSource csvDataSource = new CsvDataSource(testUtils.getResourcePath(CSV_WITHOUT_EMPTY_LINES_BUT_WITH_HEADERS));
        try {
            List<Lender> lenders = csvDataSource.getLenders();
            assertLenders(lenders);
        } catch (Exception e) {
            fail("Exception is not expected here");
        }
    }

    @Test
    public void testParseLendersWithHeadersAndEmptyLines() {
        CsvDataSource csvDataSource = new CsvDataSource(testUtils.getResourcePath(CSV_WITH_HEADERS_AND_EMPTY_LINES));
        try {
            List<Lender> lenders = csvDataSource.getLenders();
            assertLenders(lenders);
        } catch (Exception e) {
            fail("Exception is not expected here");
        }
    }

    @Test
    public void testParseLendersWhenIncompatibleCsvLineOccurs() throws IncompatibleCsvLine {
        mockStatic(CsvUtils.class);
        when(CsvUtils.getLender(anyString())).thenThrow(new IncompatibleCsvLine("Incompatible Line"));

        CsvDataSource csvDataSource = new CsvDataSource(testUtils.getResourcePath(CSV_WITH_HEADERS_AND_EMPTY_LINES));
        try {
            List<Lender> lenders = csvDataSource.getLenders();
            assertLenders(lenders);
        } catch (Exception e) {
            assertThatExceptionMatches(e, MalformedLineException.class, "The line number 1");
        }
    }

    @Test
    public void testParseLendersWhenNumberFormatExceptionOccurs() throws IncompatibleCsvLine {
        mockStatic(CsvUtils.class);
        when(CsvUtils.getLender(anyString())).thenThrow(new NumberFormatException());

        CsvDataSource csvDataSource = new CsvDataSource(testUtils.getResourcePath(CSV_WITH_HEADERS_AND_EMPTY_LINES));
        try {
            List<Lender> lenders = csvDataSource.getLenders();
            assertLenders(lenders);
        } catch (Exception e) {
            assertThat(e, instanceOf(MalformedLineException.class));
        }
    }

    @Test
    public void testParseLendersWhenNumberFormatExceptionOccursAtLine3() throws IncompatibleCsvLine {
        CsvDataSource csvDataSource = new CsvDataSource(testUtils.getResourcePath(CSV_MALFORMED_LINE_3));
        try {
            List<Lender> lenders = csvDataSource.getLenders();
            assertLenders(lenders);
        } catch (Exception e) {
            assertThatExceptionMatches(e, MalformedLineException.class, "The line number 3");
        }
    }

    private void assertThatExceptionMatches(Exception exception, Class classType, String text) {
        assertThat(exception, instanceOf(classType));
        assertTrue(exception.getMessage().contains(text));
    }

    private void assertLenders(List<Lender> lenders) {
        assertNotNull(lenders);
        assertThat(lenders.size(), is(7));
        verifyLenderAtIndex("Bob",0.075,640, 0, lenders);
        verifyLenderAtIndex("Jane",0.069,480, 1, lenders);
        verifyLenderAtIndex("Fred",0.071,520, 2, lenders);
        verifyLenderAtIndex("Mary",0.104,170, 3, lenders);
        verifyLenderAtIndex("John",0.081,320, 4, lenders);
        verifyLenderAtIndex("Dave",0.074,140, 5, lenders);
        verifyLenderAtIndex("Angela",0.071,60, 6, lenders);
    }

    private void verifyLenderAtIndex(String name, double rate, double funds, int index, List<Lender> lenders) {
        Lender lender = lenders.get(index);
        assertThat(lender.getName(), is(name));
        assertThat(lender.getRate(), is(rate));
        assertThat(lender.getFunds(), is(funds));
    }
}
