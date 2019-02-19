package com.malexj.sevice;

import static com.google.common.base.Preconditions.checkArgument;

public interface ValidationService {

  static void checkParameters(String args1, String args2) {
    String errorMessage = "The parameter %s is empty!";
    checkArgument(!args1.isEmpty(), String.format(errorMessage, args1));
    checkArgument(!args2.isEmpty(), String.format(errorMessage, args2));
  }
}
