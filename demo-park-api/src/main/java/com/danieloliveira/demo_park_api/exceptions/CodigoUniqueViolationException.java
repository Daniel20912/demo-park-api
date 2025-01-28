package com.danieloliveira.demo_park_api.exceptions;

public class CodigoUniqueViolationException extends RuntimeException {
  public CodigoUniqueViolationException(String message) {
    super(message);
  }
}
