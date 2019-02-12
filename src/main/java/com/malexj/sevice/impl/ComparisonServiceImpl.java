package com.malexj.sevice.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import com.google.common.base.Strings;
import com.malexj.model.ComparisonModel;
import com.malexj.sevice.ComparisonService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.log4j.Log4j;

@Log4j
public class ComparisonServiceImpl implements ComparisonService {

  private static final String ERROR_NULL_LIST = "The list of documents should not be null!";

  private static final String ERROR_NULL_FIELD =
      "The name of the document should not be null or empty!";

  @Override
  public Set<ComparisonModel> determineNumberOfDocuments(List<ComparisonModel> documents) {
    checkNotNull(documents, ERROR_NULL_LIST);
    return documents
        .stream()
        .filter(checkDocumentName())
        .map(this::mapComparisonModel)
        .collect(Collectors.toSet());
  }

  @Override
  public List<String> getDocumentsFromFolder(String path) throws IOException {
    List<String> documents;
    try (Stream<Path> paths = Files.walk(Paths.get(path))) {
      documents =
          paths
              .filter(Files::isRegularFile)
              .map(file -> file.getFileName().toString())
              .collect(Collectors.toList());
    }
    return documents;
  }

  private Predicate<ComparisonModel> checkDocumentName() {
    return doc -> {
      String firstDocument = doc.getFirstDocument();
      String secondDocument = doc.getSecondDocument();

      checkState(!Strings.isNullOrEmpty(firstDocument), ERROR_NULL_FIELD);
      checkState(!Strings.isNullOrEmpty(secondDocument), ERROR_NULL_FIELD);

      return !firstDocument.equals(secondDocument);
    };
  }

  private ComparisonModel mapComparisonModel(ComparisonModel doc) {
    String firstDocument = doc.getFirstDocument();
    String secondDocument = doc.getSecondDocument();
    if (firstDocument.compareTo(secondDocument) > 0) {
      doc.setFirstDocument(secondDocument);
      doc.setSecondDocument(firstDocument);
    }
    return doc;
  }
}
