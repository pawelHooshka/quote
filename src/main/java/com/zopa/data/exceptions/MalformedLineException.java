package com.zopa.data.exceptions;

import static java.util.Optional.ofNullable;

public class MalformedLineException extends Exception {

    private static final String ERROR_MESSAGE = "The line number %d in the file '%s' is malformed: '%s'";

    public MalformedLineException(int lineNumber, String filePath, String line, Throwable cause) {
        super(String.format(ERROR_MESSAGE, lineNumber, filePath, ofNullable(line).orElse("Not Found")), cause);
    }

    public MalformedLineException(int lineNumber, String filePath, String line) {
        super(String.format(ERROR_MESSAGE, lineNumber, filePath, ofNullable(line).orElse("Not Found")));
    }
}
