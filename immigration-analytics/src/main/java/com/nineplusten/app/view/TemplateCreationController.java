package com.nineplusten.app.view;

import com.sun.javafx.scene.control.skin.TableColumnHeader;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

@SuppressWarnings("restriction")
public class TemplateCreationController {

  // These items are for the combox set up
  @FXML
  private ComboBox<String> comboBox;
  @FXML
  private Label comboBoxLabel;
  @FXML
  private TextField AgencyNameText;
  @FXML
  private ListView<String> colList;
  @FXML
  private TextField newColName;
  @FXML
  private TableView<String> templateTable;

  // Properties: may need to move to model
  private TableColumnHeader selectedHeader = null;


  public TemplateCreationController() {
  }

  /**
   * This will show text for agency only
   *
   */
  @FXML
  private void comboBoxWasUpdate() {
    if (comboBox.getValue().toString() == "Agency") {
      this.comboBoxLabel.setVisible(true);
      this.AgencyNameText.setVisible(true);
    } else {
      this.comboBoxLabel.setVisible(false);
      this.AgencyNameText.setVisible(false);
    }

  }

  /**
   * this is action call for submit button
   * 
   * @param action
   */
  @FXML
  private void submitButtonAction(ActionEvent action) {
    String s = "add action here";
  }

  /**
   * this is action call for cancel button
   */
  @FXML
  private void cancelButtonAction(ActionEvent action) {
    String s = "add action here";
  }


  /**
   * This will remove button for listview
   */
  @FXML
  private void listviewRemove() {
    ObservableList<String> selectedItem = colList.getSelectionModel().getSelectedItems();
    for (String selected : selectedItem) {
      colList.getItems().remove(selected);
    }
  }

  /**
   * This will add one addtional entry to the listview
   */
  @FXML
  private void listviewAdd() {

    colList.getItems().add(newColName.getText());
  }

  /**
   * This just atuo call for the ui
   */

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
                selectedHeader.setStyle("-fx-background-color: transparent;");
                selectedHeader = tch;
                selectedHeader.setStyle("-fx-background-color: rgba(0, 147, 255, .2);");
              } else {
                selectedHeader = tch;
                selectedHeader.setStyle("-fx-background-color: rgba(0, 147, 255, .2);");
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
            // tableColumn.setPrefWidth(textField.getWidth());
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

  @FXML
  private void initialize() {
    // this is for configuring the comboBox
    comboBox.getItems().addAll("TEQ", "Agency", "All");
    comboBoxLabel.setVisible(false);
    AgencyNameText.setVisible(false);


    //
    templateTable.setPlaceholder(new Label(""));
    templateTable.skinProperty().addListener(this::installHeaderHandler);
    templateTable.getItems().add("");
    templateTable.setSelectionModel(null);
    templateTable.addEventHandler(KeyEvent.KEY_PRESSED, e-> {
      if (e.getCode() == KeyCode.DELETE) {
        delColumn();
        e.consume();
      } else if (e.getCode() == KeyCode.INSERT) {
        addColumn();
        e.consume();
      }
    });
  }

  @FXML
  private void addColumn(ActionEvent e) {
    addColumn();
  }
  
  private void addColumn() {
    TableColumn<String, String> c = new TableColumn<>("new_column");
    c.setSortable(false);
    templateTable.getColumns().add(c);
  }
  
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
}
