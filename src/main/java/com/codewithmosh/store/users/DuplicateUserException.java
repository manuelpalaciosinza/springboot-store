package com.codewithmosh.store.users;

public class DuplicateUserException extends RuntimeException {
    public DuplicateUserException(String message) {
        super(message);
    }
  public DuplicateUserException() {
    super("The users are duplicate");
  }
}
