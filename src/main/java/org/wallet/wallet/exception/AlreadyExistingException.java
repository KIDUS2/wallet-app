package org.wallet.wallet.exception;

public class AlreadyExistingException extends RuntimeException{
  public AlreadyExistingException(String message) {
    super(message);
  }
}
