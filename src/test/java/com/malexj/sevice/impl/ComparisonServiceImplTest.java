package com.malexj.sevice.impl;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;

import com.google.common.collect.Lists;
import com.malexj.model.ComparisonModel;
import com.malexj.sevice.ComparisonService;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.extern.log4j.Log4j;
import org.junit.Test;

@Log4j
public class ComparisonServiceImplTest {

  private ComparisonService service = new ComparisonServiceImpl();

  private static final String PATH_SOURCE = "documents";
  private static final String SOURCE_FILE;

  private static final String ERROR_MESSAGE = "File not found: %s";

  static {
    ClassLoader context = Thread.currentThread().getContextClassLoader();
    URL sourceRes =
        Optional.ofNullable(context.getResource(PATH_SOURCE))
            .orElseThrow(
                () -> {
                  String errorMessage = String.format(ERROR_MESSAGE, PATH_SOURCE);
                  return new IllegalArgumentException(errorMessage);
                });
    SOURCE_FILE = sourceRes.getFile();
  }

  @Test
  public void testGetDocumentsFromFolder() throws IOException {
    // given
    List<String> documentsFromFolder = service.getDocumentsFromFolder(SOURCE_FILE);

    // then
    int expectedSize = 3;
    assertEquals(expectedSize, documentsFromFolder.size());
  }

  @Test
  public void testNullGetDocumentsFromFolder() {
    try {
      service.getDocumentsFromFolder(null);
      fail();
    } catch (Exception ex) {
      // ignore
    }
  }

  @Test
  public void testExceptionGetDocumentsFromFolder() {
    try {
      service.getDocumentsFromFolder(UUID.randomUUID().toString());
      fail();
    } catch (Exception ex) {
      // ignore
    }
  }

  @Test
  public void testWhenTheModelHasSimilarFields() {
    // given
    ComparisonModel model1 = new ComparisonModel("doc1", "doc2", null);
    ComparisonModel model2 = new ComparisonModel("doc2", "doc1", null);
    List<ComparisonModel> list = Lists.newArrayList(model1, model2);

    // when
    Set<ComparisonModel> result = service.determineNumberOfDocuments(list);

    // then
    int expectedSize = 1;
    assertEquals(expectedSize, result.size());
    assertEquals(model1, result.iterator().next());
  }

  @Test
  public void testTenModels() {
    // given
    List<ComparisonModel> list = new ArrayList<>();
    int j = 10;
    for (int i = 1; i <= 10; i++) {
      ComparisonModel model = new ComparisonModel(String.valueOf(i), String.valueOf(j--), null);
      list.add(model);
    }

    // when
    Set<ComparisonModel> result = service.determineNumberOfDocuments(list);

    // then
    int expectedSize = 5;
    assertEquals(expectedSize, result.size());
  }

  @Test
  public void testTenDocuments() {
    // given
    List<ComparisonModel> list = new ArrayList<>();
    for (int i = 1; i <= 4; i++) {
      for (int j = 1; j <= 4; j++) {
        ComparisonModel model = new ComparisonModel(String.valueOf(i), String.valueOf(j), null);
        list.add(model);
      }
    }

    // when
    Set<ComparisonModel> result = service.determineNumberOfDocuments(list);

    // then
    int expectedSize = 6;
    assertEquals(expectedSize, result.size());
  }

  @Test
  public void testWhenOneOfTheModelFieldsIsEmpty() {
    // given
    ComparisonModel model1 = new ComparisonModel(null, "doc2", null);
    ComparisonModel model2 = new ComparisonModel("doc2", "doc1", null);
    List<ComparisonModel> list = Lists.newArrayList(model1, model2);

    // when
    try {
      service.determineNumberOfDocuments(list);
      fail();
    } catch (IllegalStateException ex) {
      assertEquals("The name of the document should not be null or empty!", ex.getMessage());
    }
  }

  @Test
  public void testWhenModelHasSameField() {
    // given
    ComparisonModel model1 = new ComparisonModel("doc", "doc", null);
    ComparisonModel model2 = new ComparisonModel("doc", "doc", null);
    List<ComparisonModel> list = Lists.newArrayList(model1, model2);

    // when
    Set<ComparisonModel> result = service.determineNumberOfDocuments(list);

    // then
    assertTrue(result.isEmpty());
  }

  @Test
  public void testWhenListIsNull() {
    try {
      service.determineNumberOfDocuments(null);
      fail();
    } catch (NullPointerException ex) {
      assertEquals("The list of documents should not be null!", ex.getMessage());
    }
  }

  @Test
  public void testWhenListIsEmpty() {
    // given
    List<ComparisonModel> list = new ArrayList<>();

    // when
    Set<ComparisonModel> result = service.determineNumberOfDocuments(list);

    // then
    assertTrue(result.isEmpty());
  }
}
