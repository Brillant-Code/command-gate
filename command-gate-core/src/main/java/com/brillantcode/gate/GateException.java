package com.brillantcode.gate;

/**
 * Generic exception class for errors that occurred during
 * gate initialization or execution.
 */
public class GateException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public GateException(String message) {
    super(message);
  }

}
