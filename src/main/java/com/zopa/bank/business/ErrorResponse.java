package com.zopa.bank.business;

public class ErrorResponse implements BankResponse {

    private String text;
    private Throwable cause;

    public ErrorResponse(String errorText, Object... args) {
        this.text = errorText;
        if (args.length > 0) {
            text = String.format(text, args);
        }
    }

    public ErrorResponse(String errorText, Throwable cause) {
        this.text = errorText;
        this.cause = cause;
    }

    public Throwable getCause() {
        return cause;
    }

    @Override
    public String toString() {
        return text;
    }
}
