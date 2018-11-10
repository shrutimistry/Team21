package com.nineplusten.app.view;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.nineplusten.app.App;
import com.nineplusten.app.cache.Cache;
import com.nineplusten.app.constant.Routes;
import com.nineplusten.app.model.Template;
import com.nineplusten.app.model.TemplateEditorModel;
import com.nineplusten.app.serializer.TemplateSerializer;
import com.nineplusten.app.util.RestDbIO;
import com.nineplusten.app.util.StringUtil;
import com.nineplusten.app.util.TextUtil;
import javafx.beans.Observable;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.skin.TableColumnHeader;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class TemplateCreationController {

  // These items are for the combox set up
  @FXML
  private TableView<String> templateTable;

  @FXML
  private ChoiceBox<Template> templateSelector;

  @FXML
  private VBox templateSideControls;

  @FXML
  private ButtonBar templateMainControls;

  private final String CSS_TRANSPARENT = "-fx-background-color: transparent;";
  private final String CSS_SELECTED = "-fx-background-color: rgba(0, 147, 255, .2);";
  private final String NEW_COLUMN_TEXT = "New Column";

  @SuppressWarnings("unused")
  private App mainApp;
  private TemplateEditorModel editor;
  private Gson gson;

  public TemplateCreationController() {}

  @FXML
  private void initialize() {
    gson = new GsonBuilder().registerTypeAdapter(Template.class, new TemplateSerializer()).create();
    editor = new TemplateEditorModel();
    templateSelector.getItems().addAll(Cache.templates);
    configureTemplateTable();
    templateSideControls.disableProperty().bind(templateSelector.valueProperty().isNull());
    templateMainControls.disableProperty().bind(editor.modifiedProperty().not());
    templateTable.getColumns()
        .addListener((ListChangeListener<? super TableColumn<String, ?>>) (c) -> {
          if (!editor.isModified()) {
            editor.setModified(true);
          }
        });
  }


  // This is a VERY hacky method of implementing column header editing and selection
  // There is NO built-in way of modifying or selecting the table header
  // Solution borrowed and inspired by sources below and modified to application needs:
  // https://stackoverflow.com/questions/46904932/change-value-column-header-in-tableview-javafx
  // https://stackoverflow.com/questions/12737829/javafx-textfield-resize-to-text-length/18608568

  @SuppressWarnings("unlikely-arg-type")
  protected void installHeaderHandler(Observable s) {
    templateTable.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
      if (e.isPrimaryButtonDown() && e.getClickCount() == 1) {
        EventTarget target = e.getTarget();
        while (target instanceof Node) {
          target = ((Node) target).getParent();
          if (target instanceof TableColumnHeader) {
            TableColumnHeader tch = (TableColumnHeader) target;
            if (!templateTable.getColumns().contains(tch)) {
              if (editor.getSelectedHeader() != null) {
                editor.getSelectedHeader().setStyle(CSS_TRANSPARENT);
                editor.setSelectedHeader(tch);
                editor.getSelectedHeader().setStyle(CSS_SELECTED);
              } else {
                editor.setSelectedHeader(tch);
                editor.getSelectedHeader().setStyle(CSS_SELECTED);
              }
              break;
            }
          }
        }
      }
      if (e.isPrimaryButtonDown() && e.getClickCount() > 1) {
        EventTarget target = e.getTarget();
        TableColumnBase<?, ?> column = null;
        while (target instanceof Node) {
          target = ((Node) target).getParent();
          if (target instanceof TableColumnHeader) {
            column = ((TableColumnHeader) target).getTableColumn();
            if (column != null)
              break;
          }
        }
        if (column != null) {
          TableColumnBase<?, ?> tableColumn = column;
          final String oldVal = column.getText();
          TextField textField = new TextField(column.getText());
          textField.setPrefWidth(column.getWidth());
          textField.textProperty().addListener((ov, prevText, currText) -> {
            Text text = new Text(currText);
            text.setFont(textField.getFont());
            double width = text.getLayoutBounds().getWidth() + textField.getPadding().getLeft()
                + textField.getPadding().getRight() + 2d;
            textField.setPrefWidth(width);
            textField.positionCaret(textField.getCaretPosition());
            tableColumn.setPrefWidth(width);
          });
          textField.setOnAction(a -> {
            tableColumn.setText(textField.getText());
            tableColumn.setGraphic(null);
            tableColumn.setPrefWidth(textField.getWidth());
            if (!oldVal.equals(textField.getText())) {
              if (!editor.isModified()) {
                editor.setModified(true);
              }
              editor.putColumnId(tableColumn, StringUtil.toSnakeCase(textField.getText()));
            }
          });
          textField.focusedProperty().addListener((src, ov, nv) -> {
            if (!nv) {
              tableColumn.setText(textField.getText());
              tableColumn.setGraphic(null);
              tableColumn.setPrefWidth(textField.getWidth());
              if (!oldVal.equals(textField.getText())) {
                if (!editor.isModified()) {
                  editor.setModified(true);
                }
                editor.putColumnId(tableColumn, StringUtil.toSnakeCase(textField.getText()));
              }
            }
          });
          column.setText(" ");
          column.setGraphic(textField);
          textField.requestFocus();
        }
        e.consume();
      }
    });
  }

  private void configureTemplateTable() {
    templateTable.setPlaceholder(new Label(""));
    templateTable.skinProperty().addListener(this::installHeaderHandler);
    templateTable.getItems().add("");
    templateTable.setSelectionModel(null);
    templateSelector.valueProperty().addListener((src, oldVal, newVal) -> {
      // Clear all existing data related to columns
      templateTable.getColumns().clear();
      editor.columnIdMapProperty().clear();

      List<TableColumn<String, String>> columns =
          newVal.getColumns().entrySet().stream().map(entry -> {
            TableColumn<String, String> t = new TableColumn<>(entry.getValue());
            t.setResizable(false);
            t.setSortable(false);
            t.setPrefWidth(TextUtil.getTextWidth(entry.getValue()));
            // Map physical column to column id
            editor.columnIdMapProperty().put(t, entry.getKey());
            return t;
          }).collect(Collectors.toList());
      templateTable.getColumns().addAll(columns);
      editor.setModified(false);
      editor.setReferenceTemplate(newVal);
    });
    templateTable.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
      if (templateSelector.getValue() != null) {
        if (e.getCode() == KeyCode.DELETE) {
          delColumn();
          e.consume();
        } else if (e.getCode() == KeyCode.INSERT) {
          addColumn();
          e.consume();
        }
      }
    });
    /*
     * templateTable.skinProperty().addListener((obs, oldSkin, newSkin) -> { final TableHeaderRow
     * header = (TableHeaderRow) templateTable.lookup("TableHeaderRow");
     * header.reorderingProperty().addListener((o, oldVal, newVal) -> header.setReordering(false));
     * });
     */
  }

  @FXML
  private void addColumn(ActionEvent e) {
    addColumn();
  }

  private void addColumn() {

    TableColumn<String, String> c = new TableColumn<>(NEW_COLUMN_TEXT);
    c.setSortable(false);
    c.setResizable(false);
    c.setPrefWidth(TextUtil.getTextWidth(c.getText()));
    editor.putColumnId(c, StringUtil.toSnakeCase(NEW_COLUMN_TEXT));
    templateTable.getColumns().add(c);
  }

  /*
   * private void createColumns(List<String> columnNames) { List<TableColumn<String, String>>
   * columnList = columnNames.stream() .map(name -> new TableColumn<String,
   * String>(name)).collect(Collectors.toList()); columnList.forEach(col -> {
   * col.setSortable(false); col.setResizable(false); col.setPrefWidth(getTextWidth(col.getText()));
   * }); templateTable.getColumns().clear(); templateTable.getColumns().addAll(columnList); }
   */

  @FXML
  private void delColumn(ActionEvent e) {
    delColumn();
  }

  private void delColumn() {
    if (editor.getSelectedHeader() != null) {
      TableColumnBase<?, ?> c = editor.getSelectedHeader().getTableColumn();
      editor.columnIdMapProperty().remove(c);
      templateTable.getColumns().remove(c);
      editor.setSelectedHeader(null);
    }
  }

  /*
   * @FXML private void loadExcel(ActionEvent e) { FileChooser chooser = new FileChooser();
   * chooser.setTitle("Select ICARE Template"); chooser.getExtensionFilters().addAll(new
   * ExtensionFilter("Excel Files (.xlsx)", "*.xlsx")); File selectedFile =
   * chooser.showOpenDialog(mainApp.getPrimaryStage()); if (selectedFile != null) {
   * excelPath.set(selectedFile.getAbsolutePath()); if (!excelService.isRunning()) {
   * excelService.restart(); } } }
   */

  public void setMainApp(App mainApp) {
    this.mainApp = mainApp;
  }

  @FXML
  private void save() {
    Alert alert = new Alert(AlertType.CONFIRMATION);
    alert.setTitle("Save Confirmation");
    alert.setHeaderText(
        "Save changes to \"" + templateSelector.getValue().getTemplateName() + "\"?");
    alert.setContentText(
        "This will override the existing template. Agencies will be able to view changes to this template immediately.");
    alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);

    Optional<ButtonType> result = alert.showAndWait();
    if (result.get() == ButtonType.OK) {
      Map<String, String> columns = new LinkedHashMap<>();
      templateTable.getColumns().stream().forEach(col -> {
        columns.put(editor.getColumnId(col), col.getText());
      });
      Template t = templateSelector.getValue();
      t.setColumns(columns);
      try {
        RestDbIO.put(Routes.TEMPLATES, t.get_id(), gson.toJson(t));
      } catch (UnirestException e) {
        e.printStackTrace();
      }
      editor.setReferenceTemplate(t);
      editor.setModified(false);
    }

  }

  @FXML
  private void discardChanges() {
    editor.columnIdMapProperty().clear();
    templateTable.getColumns().clear();
    List<TableColumn<String, String>> columns =
        editor.getReferenceTemplate().getColumns().entrySet().stream().map(entry -> {
          TableColumn<String, String> t = new TableColumn<>(entry.getValue());
          t.setResizable(false);
          t.setSortable(false);
          t.setPrefWidth(TextUtil.getTextWidth(entry.getValue()));
          // Map physical column to column id
          editor.columnIdMapProperty().put(t, entry.getKey());
          return t;
        }).collect(Collectors.toList());
    templateTable.getColumns().addAll(columns);
    editor.setModified(false);
  }

  /*
   * private void configureExcelService() { excelPath = new SimpleStringProperty(""); excelService =
   * new LoadExcelService(excelPath); excelService.setOnSucceeded(new
   * EventHandler<WorkerStateEvent>() {
   * 
   * @Override public void handle(WorkerStateEvent event) { createColumns(excelService.getValue());
   * } }); excelService.setOnFailed(new EventHandler<WorkerStateEvent>() {
   * 
   * @Override public void handle(WorkerStateEvent event) { Throwable throwable =
   * excelService.getException(); throwable.printStackTrace(); } });
   * excelLoadIndicator.visibleProperty().bind(excelService.runningProperty());
   * excelBrowse.visibleProperty().bind(excelService.runningProperty().not());
   * excelEditor.disableProperty().bind(excelService.runningProperty()); }
   */
}
