package com.web10.taskmanagement.exceptions;

//ExpiredTokenException.java

public class ExpiredTokenException extends RuntimeException {

 /**
  * Constructs a new exception with the specified detail message.
  *
  * @param message the detail message
  */
 public ExpiredTokenException(String message) {
     super(message);
 }

 /**
  * Constructs a new exception with the specified detail message and cause.
  *
  * @param message the detail message
  * @param cause   the cause
  */
 public ExpiredTokenException(String message, Throwable cause) {
     super(message, cause);
 }
}
