package com.zopa.data.fileutils;

import com.zopa.data.entity.Lender;
import com.zopa.data.exceptions.IncompatibleCsvLine;

public class CsvUtils {

    public static Lender getLender(String csvLine) throws IncompatibleCsvLine {
        if (csvLine == null || csvLine.isEmpty()) {
            return null;
        }
        return parse(csvLine);
    }

    private static Lender parse(String csvLine) throws IncompatibleCsvLine {
        String[] line = csvLine.split(",");

        if (line.length == 3) {
            String name = line[0].trim();
            double rate = Double.parseDouble(line[1].trim());
            double funds = Double.parseDouble(line[2].trim());
            return new Lender(name, rate, funds);
        } else if (line.length != 0) {
            throw new IncompatibleCsvLine(csvLine);
        }
        return null;
    }
}
