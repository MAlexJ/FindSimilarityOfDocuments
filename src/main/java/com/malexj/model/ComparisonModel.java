package com.malexj.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class ComparisonModel {
  private String firstDocument;
  private String secondDocument;

  private Statistics statistics;
}
