package com.malexj.sevice;

import com.malexj.model.ComparisonModel;
import com.malexj.model.DiffStatistics;

import java.util.List;
import java.util.Set;

public interface ComparisonService {

  /**
   * @param path the path to the folder with files
   * @return list of all models
   */
  List<ComparisonModel> findAllModelsFromFolder(String path);

  /**
   * @param models
   * @return
   */
  Set<ComparisonModel> determineNumberOfModels(List<ComparisonModel> models);

  /**
   * Compute the difference between to files
   *
   * @param models
   * @return set of models
   */
  Set<DiffStatistics> computeDifferenceBetweenFiles(Set<ComparisonModel> models);

  /**
   * Write statistics to file
   *
   * @param statistics set of statistics
   * @param path he path to destination folder
   */
  void writeDiffStatisticsToFile(Set<DiffStatistics> statistics, String path);
}
