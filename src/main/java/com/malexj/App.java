package com.malexj;

import com.malexj.model.ComparisonModel;
import com.malexj.model.DiffStatistics;
import com.malexj.sevice.ComparisonService;
import com.malexj.sevice.impl.ComparisonServiceImpl;
import lombok.extern.log4j.Log4j;

import java.util.List;
import java.util.Set;

import static com.malexj.sevice.ValidationService.checkParameters;

/** */
@Log4j
public class App {

  private static final String ERROR_MISSING_PATH =
      "Missing path to source folder and destination folder";

  private static ComparisonService service;

  static {
    service = new ComparisonServiceImpl();
  }

  public static void main(String[] args) {
    log.debug("Run app");

    if (args.length != 2) {
      log.error(ERROR_MISSING_PATH);
      System.exit(1);
    }

    String sourceFolder = args[0];
    String destinationFolder = args[1];

    log.debug("Source folder: " + sourceFolder);
    log.debug("Destination folder: " + destinationFolder);

    checkParameters(sourceFolder, destinationFolder);

    List<ComparisonModel> allModelsFromFolder = service.findAllModelsFromFolder(sourceFolder);

    Set<ComparisonModel> comparisonModels = service.determineNumberOfModels(allModelsFromFolder);

    Set<DiffStatistics> diffStatistics = service.computeDifferenceBetweenFiles(comparisonModels);

    service.writeDiffStatisticsToFile(diffStatistics, destinationFolder);

    log.debug("Complete");
  }
}
