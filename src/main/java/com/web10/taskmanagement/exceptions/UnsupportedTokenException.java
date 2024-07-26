package com.web10.taskmanagement.exceptions;

//UnsupportedTokenException.java

public class UnsupportedTokenException extends RuntimeException {

 /**
  * Constructs a new exception with the specified detail message.
  *
  * @param message the detail message
  */
 public UnsupportedTokenException(String message) {
     super(message);
 }

 /**
  * Constructs a new exception with the specified detail message and cause.
  *
  * @param message the detail message
  * @param cause   the cause
  */
 public UnsupportedTokenException(String message, Throwable cause) {
     super(message, cause);
 }
}