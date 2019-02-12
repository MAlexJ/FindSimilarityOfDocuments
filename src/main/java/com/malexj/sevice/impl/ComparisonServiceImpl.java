package com.malexj.sevice.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import com.google.common.base.Strings;
import com.malexj.model.ComparisonModel;
import com.malexj.sevice.ComparisonService;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
        .map(this::mapDocumentName)
        .collect(Collectors.toSet());
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

  private ComparisonModel mapDocumentName(ComparisonModel doc) {
    String firstDocument = doc.getFirstDocument();
    String secondDocument = doc.getSecondDocument();
    if (firstDocument.compareTo(secondDocument) > 0) {
      doc.setFirstDocument(secondDocument);
      doc.setSecondDocument(firstDocument);
    }
    return doc;
  }
}
