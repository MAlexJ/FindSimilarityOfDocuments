package com.malexj.model;

import difflib.Delta;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DiffStatistics {

  private String firstFileName;
  private String secondFileName;
  private List<Delta<String>> deltas;

  public DiffStatistics(ComparisonModel model, List<Delta<String>> differenceDeltas) {
    this.firstFileName = model.getFirstFileName();
    this.secondFileName = model.getSecondFileName();
    this.deltas = differenceDeltas;
  }
}
