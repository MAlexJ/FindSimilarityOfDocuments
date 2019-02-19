package com.malexj.model;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude = {"firstFile", "secondFile"})
@EqualsAndHashCode(exclude = {"firstFile", "secondFile"})
public class ComparisonModel {

  private static final String ERROR_NULL_FILE = "The file should not be null!";

  private File firstFile;
  private File secondFile;

  private String firstFileName;
  private String secondFileName;

  public ComparisonModel(File firstFile, File secondFile) {
    init(firstFile, secondFile);
  }

  private void init(File firstFile, File secondFile) {
    checkNotNull(firstFile, ERROR_NULL_FILE);
    checkNotNull(secondFile, ERROR_NULL_FILE);

    if (firstFile.toString().compareTo(secondFile.toString()) > 0) {
      this.firstFile = secondFile;
      this.secondFile = firstFile;
    } else {
      this.firstFile = firstFile;
      this.secondFile = secondFile;
    }

    initFields();
  }

  private void initFields() {
    this.firstFileName = firstFile.getName();
    this.secondFileName = secondFile.getName();
  }
}
