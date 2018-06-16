package com.zopa.data;

import com.zopa.data.fileutils.CsvUtils;
import com.zopa.data.entity.Lender;
import com.zopa.data.exceptions.IncompatibleCsvLine;
import com.zopa.data.exceptions.MalformedLineException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvDataSource {

    private final static Logger logger = LoggerFactory.getLogger(CsvDataSource.class);
    private String filePath;

    public CsvDataSource(String filePath) {
        this.filePath = filePath;
    }

    public List<Lender> getLenders() throws MalformedLineException {
        int lineNumber = 1;
        String line = null;

        List<Lender> lenders = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            line = reader.readLine();
            Line lineObject = skipLineIfCsvHeadersArePresent(reader, line, lineNumber); //We do that to skip CSV headers
                                                                            // (Lender,Rate,Available)
                                                                            //if these are present!
            line = lineObject.text;
            lineNumber = lineObject.lineNumber;
            while (line != null) {
                addLenderToLendersList(lenders, line);
                lineNumber++;
                line = reader.readLine();
            }
        } catch (FileNotFoundException e) {
            logger.error("File '{}' was not found", filePath, e);
        } catch (IOException e) {
            logger.error("Could not read file '{}'", filePath, e);
        } catch (NumberFormatException | IncompatibleCsvLine e) {
            logger.error("Error while processing line number {} in the file '{}'", e);
            throw new MalformedLineException(lineNumber, filePath, line, e);
        } catch (Exception e) {
            logger.error("Could not process file '{}'", filePath, e);
            throw e;
        }

        return lenders;
    }

    private void addLenderToLendersList(List<Lender> lenders, String line) throws IncompatibleCsvLine {
        Lender lender = CsvUtils.getLender(line);
        if (lender != null) {
            lenders.add(lender);
        }
    }

    private Line skipLineIfCsvHeadersArePresent(BufferedReader reader, String line, int currentLinePosition)
        throws IOException, IncompatibleCsvLine {
        try {
            CsvUtils.getLender(line);
            return new Line(line, currentLinePosition);
        } catch (NumberFormatException e) {
            //This is ok, we expect the number format exception to occur if the CSV file does have a headers
            //- values like: Lender,Rate,Available cannot be parsed into a number. These are not numbers.
            return new Line(reader.readLine(), ++currentLinePosition);
        }
    }

    private class Line {
        private String text;
        private int lineNumber;

        Line(String text, int lineNumber) {
            this.text = text;
            this.lineNumber = lineNumber;
        }
    }
}
