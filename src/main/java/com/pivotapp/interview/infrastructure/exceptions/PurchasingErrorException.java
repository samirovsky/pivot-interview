package com.pivotapp.interview.infrastructure.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class PurchasingErrorException extends RuntimeException {
  public PurchasingErrorException(String message) {
    super(message);
  }
}
