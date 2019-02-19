package com.malexj.exception;

import static java.lang.String.format;

import javax.annotation.Nonnull;

public class AppException extends RuntimeException {

  private final transient Object[] args;

  public AppException(@Nonnull final String messageTemplate, @Nonnull final Object... args) {
    super(messageTemplate);
    this.args = args;
  }

  @Override
  public String getMessage() {
    return format(super.getMessage(), args);
  }
}
