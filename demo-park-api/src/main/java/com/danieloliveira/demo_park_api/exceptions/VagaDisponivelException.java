package com.danieloliveira.demo_park_api.exceptions;

public class VagaDisponivelException extends RuntimeException {
    public VagaDisponivelException(String message) {
        super(message);
    }
}
