package com.zopa.data.exceptions;

import static java.util.Optional.ofNullable;

public class IncompatibleCsvLine extends Exception {

    private static final String ERROR_MESSAGE = "incomplete CSV line '%s'";

    public IncompatibleCsvLine(String line) {
        super(String.format(ERROR_MESSAGE, ofNullable(line).orElse("Not Found")));
    }
}
