package com.malexj.sevice.impl;

import com.google.common.collect.Lists;
import com.malexj.model.ComparisonModel;
import com.malexj.model.DiffStatistics;
import lombok.extern.log4j.Log4j;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.*;

import static junit.framework.TestCase.*;

@Log4j
public class ComparisonServiceImplTest {

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

  private ComparisonServiceImpl service = new ComparisonServiceImpl();

  @Test
  public void testGetDocumentsFromFolder() {
    // given
    List<ComparisonModel> models = service.findAllModelsFromFolder(SOURCE_FILE);

    // then
    int expectedSize = 9;
    assertEquals(expectedSize, models.size());
  }

  @Test
  public void testExceptionWhenNullPathParam() {
    try {
      service.findAllModelsFromFolder(null);
      fail();
    } catch (Exception ex) {
      // ignore
    }
  }

  @Test
  public void testExceptionIncorrectPathParam() {
    try {
      service.findAllModelsFromFolder(UUID.randomUUID().toString());
      fail();
    } catch (Exception ex) {
      // ignore
    }
  }

  @Test
  public void testWhenModelHasSimilarFields() {
    // given
    List<ComparisonModel> modelList = service.findAllModelsFromFolder(SOURCE_FILE);
    ComparisonModel model1 = modelList.get(1);
    ComparisonModel model2 = modelList.get(0);

    // when
    Set<ComparisonModel> modelSet =
        service.determineNumberOfModels(Lists.newArrayList(model1, model2));

    // then
    int expectedSize = 1;
    assertEquals(expectedSize, modelSet.size());
    assertEquals(model1, modelSet.iterator().next());
  }

  @Test
  public void testWhenOneOfTheModelFieldsIsEmpty() {
    try {
      new ComparisonModel(null, new File("text"));
      fail();
    } catch (NullPointerException ex) {
      assertEquals("The file should not be null!", ex.getMessage());
    }
  }

  @Test
  public void testWhenModelHasEqualsField() {
    // given
    List<ComparisonModel> modelList = service.findAllModelsFromFolder(SOURCE_FILE);
    ComparisonModel model1 = modelList.get(0);
    ComparisonModel model2 = modelList.get(0);

    // when
    Set<ComparisonModel> modelSet =
        service.determineNumberOfModels(Lists.newArrayList(model1, model2));

    // then
    assertTrue(modelSet.isEmpty());
  }

  @Test
  public void testWhenListIsEmpty() {
    // when
    Set<ComparisonModel> result = service.determineNumberOfModels(new ArrayList<>());

    // then
    assertTrue(result.isEmpty());
  }

  @Test
  public void testDifference() {
    // given
    List<ComparisonModel> documents = service.findAllModelsFromFolder(SOURCE_FILE);
    Set<ComparisonModel> comparisonModelSet = service.determineNumberOfModels(documents);

    // when
    Set<DiffStatistics> diffStatistics = service.computeDifferenceBetweenFiles(comparisonModelSet);

    diffStatistics.forEach(
        m -> {
          System.out.println(m);
          System.out.println("F: " + m.getFirstFileName());
          System.out.println("S: " + m.getSecondFileName());
          m.getDeltas().forEach(System.out::println);
          System.out.println();
        });

    // then
    int expectedSize = 3;
    assertEquals(expectedSize, diffStatistics.size());
  }
}
