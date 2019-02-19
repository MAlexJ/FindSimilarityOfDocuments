package com.malexj.sevice.impl;

import com.malexj.exception.AppException;
import com.malexj.model.ComparisonModel;
import com.malexj.model.DiffStatistics;
import com.malexj.sevice.ComparisonService;
import difflib.Delta;
import difflib.DiffUtils;
import lombok.extern.log4j.Log4j;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Log4j
public class ComparisonServiceImpl implements ComparisonService {

  @Override
  public List<ComparisonModel> findAllModelsFromFolder(String path) {
    try (Stream<Path> paths = Files.walk(Paths.get(path))) {
      final List<File> files =
          paths.filter(Files::isRegularFile).map(Path::toFile).collect(Collectors.toList());
      return fillModels(files);
    } catch (IOException ex) {
      String errorMessage = ex.getMessage();
      log.error(errorMessage);
      throw new AppException(errorMessage);
    }
  }

  @Override
  public Set<ComparisonModel> determineNumberOfModels(List<ComparisonModel> models) {
    return models.stream().filter(isNotEquals()).collect(Collectors.toSet());
  }

  @Override
  public Set<DiffStatistics> computeDifferenceBetweenFiles(Set<ComparisonModel> models) {
    return models
        .stream()
        .map(model -> new DiffStatistics(model, getDifferenceDeltas(model)))
        .collect(Collectors.toSet());
  }

  private static final String FILE_TEMPLATE_FORMAT = "%s%sresult_%s.txt";

  @Override
  public void writeDiffStatisticsToFile(Set<DiffStatistics> statistics, String path) {

    Path pathDir = findDestinationDirectory(path);

    String file =
        String.format(FILE_TEMPLATE_FORMAT, pathDir, File.separator, new Date().getTime());
    try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)))) {
      for (DiffStatistics st : statistics) {
        writer.write(st.getFirstFileName());
        writer.write(" ");
        writer.write(st.getSecondFileName());
        writer.write("\n");
      }
    } catch (IOException ex) {
      String errorMessage = ex.getMessage();
      log.error(errorMessage);
      throw new AppException(errorMessage);
    }
  }

  private Path findDestinationDirectory(String path) {
    Path pathDir = Paths.get(path);
    if (!pathDir.toFile().exists()) {
      try {
        Files.createDirectory(pathDir);
      } catch (IOException ex) {
        String errorMessage = ex.getMessage();
        log.error(errorMessage);
        throw new AppException(errorMessage);
      }
    }
    return pathDir;
  }

  private List<ComparisonModel> fillModels(List<File> files) {
    List<ComparisonModel> models = new ArrayList<>();
    IntStream.range(0, files.size())
        .forEach(
            i -> {
              for (File file : files) {
                ComparisonModel model = new ComparisonModel(files.get(i), file);
                models.add(model);
              }
            });
    return models;
  }

  private Predicate<ComparisonModel> isNotEquals() {
    return model -> {
      String firstDocument = model.getFirstFileName();
      String secondDocument = model.getSecondFileName();
      return !firstDocument.equals(secondDocument);
    };
  }

  private List<Delta<String>> getDifferenceDeltas(ComparisonModel model) {
    final List<String> originalFileLines = readLines(model.getFirstFile());
    final List<String> revisedFileLines = readLines(model.getSecondFile());
    return DiffUtils.diff(originalFileLines, revisedFileLines).getDeltas();
  }

  private List<String> readLines(File file) {
    try (BufferedReader in = new BufferedReader(new FileReader(file))) {
      List<String> lines = new ArrayList<>();
      String line;

      while ((line = in.readLine()) != null) {
        lines.add(line);
      }

      return lines;
    } catch (Exception ex) {
      String errorMessage = ex.getMessage();
      log.error(errorMessage);
      throw new AppException(errorMessage);
    }
  }
}
