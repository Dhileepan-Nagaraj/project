package com.web10.taskmanagement.exceptions;

//InvalidTokenException.java

public class InvalidTokenException extends RuntimeException {

 /**
  * Constructs a new exception with the specified detail message.
  *
  * @param message the detail message
  */
 public InvalidTokenException(String message) {
     super(message);
 }

 /**
  * Constructs a new exception with the specified detail message and cause.
  *
  * @param message the detail message
  * @param cause   the cause
  */
 public InvalidTokenException(String message, Throwable cause) {
     super(message, cause);
 }
}