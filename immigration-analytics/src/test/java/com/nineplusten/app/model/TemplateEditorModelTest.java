package com.nineplusten.app.model;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import javafx.collections.ObservableMap;
import javafx.scene.control.TableColumn;

public class TemplateEditorModelTest {
  TemplateEditorModel model;

  @BeforeEach
  void configModel() {
    model = new TemplateEditorModel();
  }

  @Test
  @DisplayName("Insert column with same ID, should create new ID")
  void putColumnId_sameColumnIdDouble() {
    // New column to be inserted into Column Name-> Column ID map
    TableColumn<String, ?> column1 = new TableColumn<>();
    TableColumn<String, ?> column2 = new TableColumn<>();
    model.putColumnId(column1, "test");
    model.putColumnId(column2, "test");

    ObservableMap<?, ?> columnIdMap = model.columnIdMapProperty().get();
    boolean containsBoth = columnIdMap.containsValue("test") && columnIdMap.containsValue("test_1");
    assertTrue(containsBoth);
  }

  @Test
  @DisplayName("Insert column with same IDs, should create new IDs")
  void putColumnId_sameColumnIdMulti() {
    // New column to be inserted into Column Name-> Column ID map
    TableColumn<String, ?> column1 = new TableColumn<>();
    TableColumn<String, ?> column2 = new TableColumn<>();
    TableColumn<String, ?> column3 = new TableColumn<>();

    model.putColumnId(column1, "test");
    model.putColumnId(column2, "test");
    model.putColumnId(column3, "test");

    ObservableMap<?, ?> columnIdMap = model.columnIdMapProperty().get();
    boolean containsAll = columnIdMap.containsValue("test") && columnIdMap.containsValue("test_1")
        && columnIdMap.containsValue("test_2");
    assertTrue(containsAll);
  }

  @Test
  @DisplayName("Insert column with same and different IDs")
  void putColumnId_sameAndDiffColumnIdsMulti() {
    // New column to be inserted into Column Name-> Column ID map
    TableColumn<String, ?> column1 = new TableColumn<>();
    TableColumn<String, ?> column2 = new TableColumn<>();
    TableColumn<String, ?> column3 = new TableColumn<>();

    model.putColumnId(column1, "test");
    model.putColumnId(column2, "test");
    model.putColumnId(column3, "test123");

    ObservableMap<?, ?> columnIdMap = model.columnIdMapProperty().get();
    boolean containsAll = columnIdMap.containsValue("test") && columnIdMap.containsValue("test_1")
        && columnIdMap.containsValue("test123");
    assertTrue(containsAll);
  }
}
