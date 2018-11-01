package com.nineplusten.app.view;

import java.util.List;
import java.util.stream.Collectors;
import com.nineplusten.app.App;
import com.nineplusten.app.cache.Cache;
import com.nineplusten.app.model.Template;
import com.sun.javafx.scene.control.skin.TableColumnHeader;
import com.sun.javafx.scene.control.skin.TableHeaderRow;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

@SuppressWarnings("restriction")
public class TemplateCreationController {

  // These items are for the combox set up
  @FXML
  private TableView<String> templateTable;
  
  @FXML
  private ChoiceBox<Template> templateSelector;

  private final String CSS_TRANSPARENT = "-fx-background-color: transparent;";
  private final String CSS_SELECTED = "-fx-background-color: rgba(0, 147, 255, .2);";

  private App mainApp;
  private TableColumnHeader selectedHeader = null;


  public TemplateCreationController() {}

  @FXML
  private void initialize() {
    templateSelector.getItems().addAll(Cache.templates);
    configureTemplateTable();
  }

  /**
   * this is action call for submit button
   * 
   * @param action
   */
  @FXML
  private void submitButtonAction(ActionEvent action) {
    // add action here
  }

  /**
   * this is action call for cancel button
   */
  @FXML
  private void cancelButtonAction(ActionEvent action) {
    // add action here
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
              if (selectedHeader != null) {
                selectedHeader.setStyle(CSS_TRANSPARENT);
                selectedHeader = tch;
                selectedHeader.setStyle(CSS_SELECTED);
              } else {
                selectedHeader = tch;
                selectedHeader.setStyle(CSS_SELECTED);
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
          });
          textField.focusedProperty().addListener((src, ov, nv) -> {
            if (!nv) {
              tableColumn.setText(textField.getText());
              tableColumn.setGraphic(null);
              tableColumn.setPrefWidth(textField.getWidth());
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
      templateTable.getColumns().clear();
      List<TableColumn<String, String>> columns =
          newVal.getColumns().values().stream().map(name -> {
            TableColumn<String, String> t = new TableColumn<>(name);
            t.setResizable(false);
            t.setSortable(false);
            t.setPrefWidth(getTextWidth(name));
            return t;
          }).collect(Collectors.toList());
      templateTable.getColumns().addAll(columns);
    });
    templateTable.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
      if (e.getCode() == KeyCode.DELETE) {
        delColumn();
        e.consume();
      } else if (e.getCode() == KeyCode.INSERT) {
        addColumn();
        e.consume();
      }
    });
    /*templateTable.skinProperty().addListener((obs, oldSkin, newSkin) -> {
      final TableHeaderRow header = (TableHeaderRow) templateTable.lookup("TableHeaderRow");
      header.reorderingProperty().addListener((o, oldVal, newVal) -> header.setReordering(false));
  });*/
  }

  @FXML
  private void addColumn(ActionEvent e) {
    addColumn();
  }

  private void addColumn() {
    TableColumn<String, String> c = new TableColumn<>("new_column");
    c.setSortable(false);
    c.setResizable(false);
    c.setPrefWidth(getTextWidth(c.getText()));
    templateTable.getColumns().add(c);
  }

  /*private void createColumns(List<String> columnNames) {
    List<TableColumn<String, String>> columnList = columnNames.stream()
        .map(name -> new TableColumn<String, String>(name)).collect(Collectors.toList());
    columnList.forEach(col -> {
      col.setSortable(false);
      col.setResizable(false);
      col.setPrefWidth(getTextWidth(col.getText()));
    });
    templateTable.getColumns().clear();
    templateTable.getColumns().addAll(columnList);
  }*/

  @FXML
  private void delColumn(ActionEvent e) {
    delColumn();
  }

  private void delColumn() {
    if (selectedHeader != null) {
      templateTable.getColumns().remove(selectedHeader.getTableColumn());
      selectedHeader = null;
    }
  }

 /* @FXML
  private void loadExcel(ActionEvent e) {
    FileChooser chooser = new FileChooser();
    chooser.setTitle("Select ICARE Template");
    chooser.getExtensionFilters().addAll(new ExtensionFilter("Excel Files (.xlsx)", "*.xlsx"));
    File selectedFile = chooser.showOpenDialog(mainApp.getPrimaryStage());
    if (selectedFile != null) {
      excelPath.set(selectedFile.getAbsolutePath());
      if (!excelService.isRunning()) {
        excelService.restart();
      }
    }
  }*/

  public void setMainApp(App mainApp) {
    this.mainApp = mainApp;
  }

  /*private void configureExcelService() {
    excelPath = new SimpleStringProperty("");
    excelService = new LoadExcelService(excelPath);
    excelService.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
      @Override
      public void handle(WorkerStateEvent event) {
        createColumns(excelService.getValue());
      }
    });
    excelService.setOnFailed(new EventHandler<WorkerStateEvent>() {
      @Override
      public void handle(WorkerStateEvent event) {
        Throwable throwable = excelService.getException();
        throwable.printStackTrace();
      }
    });
    excelLoadIndicator.visibleProperty().bind(excelService.runningProperty());
    excelBrowse.visibleProperty().bind(excelService.runningProperty().not());
    excelEditor.disableProperty().bind(excelService.runningProperty());
  }*/

  private double getTextWidth(String str) {
    Text text = new Text(str);
    text.setFont(new Font(16));
    return text.getLayoutBounds().getWidth() + 2d;
  }
}
