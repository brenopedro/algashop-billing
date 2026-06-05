package com.algaworks.algashop.billing.presentation;

public class UnprocessableContentException extends RuntimeException {

    public UnprocessableContentException() {
    }

    public UnprocessableContentException(String message) {
        super(message);
    }

    public UnprocessableContentException(String message, Throwable cause) {
        super(message, cause);
    }
}
