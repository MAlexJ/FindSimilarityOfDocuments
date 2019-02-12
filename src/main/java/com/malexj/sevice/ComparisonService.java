package com.malexj.sevice;

import com.malexj.model.ComparisonModel;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface ComparisonService {

  /** Determine the effective number of documents to compare */
  Set<ComparisonModel> determineNumberOfDocuments(List<ComparisonModel> documents);

  /** Get all the documents from the folder */
  List<String> getDocumentsFromFolder(String path) throws IOException;
}
