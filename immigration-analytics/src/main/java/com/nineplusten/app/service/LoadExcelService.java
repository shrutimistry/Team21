package com.nineplusten.app.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import com.nineplusten.app.model.Agency;
import com.nineplusten.app.model.Template;
import com.nineplusten.app.model.TemplateData;
import com.nineplusten.app.util.EqualUtil;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class LoadExcelService extends Service<ObservableList<TemplateData>> {
  private ReadOnlyStringProperty filePath;
  private Agency agency;
  private ReadOnlyObjectProperty<Template> template;
  private final String UNIQUE_ID_FIELD = "Unique Identifier Value";

  public LoadExcelService(ReadOnlyStringProperty filePath, Agency agency,
      ReadOnlyObjectProperty<Template> template) {
    this.filePath = filePath;
    this.agency = agency;
    this.template = template;
  }

  @Override
  protected Task<ObservableList<TemplateData>> createTask() {
    return new Task<ObservableList<TemplateData>>() {
      @Override
      protected ObservableList<TemplateData> call() throws Exception {
        ObservableList<TemplateData> allTemplateData = FXCollections.observableArrayList();
        DataFormatter df = new DataFormatter();

        Workbook excelDoc = loadExcel(filePath.get());
        Sheet sheet = excelDoc.getSheetAt(0);
        if (sheet != null) {
          int maxCells = computeCellCount(sheet);
          int cellCount = 0;

          updateProgress(cellCount, maxCells);
          ArrayList<String> columnIds = new ArrayList<String>();
          Iterator<Row> rowIterator = sheet.iterator();

          while (rowIterator.hasNext()) {
            Row currentRow = rowIterator.next();
            boolean empty = true;
            String clientId = "";

            if (currentRow.getRowNum() >= 3) {
              Map<String, String> rowDataMap = new HashMap<String, String>();
              for (int j = 0; j < columnIds.size(); j++) {
                Cell c = currentRow.getCell(j);
                if (c != null) {
                  String value = df.formatCellValue(c);
                  if (!EqualUtil.isEmpty(value) && empty) {
                    empty = false;
                  }
                  rowDataMap.put(columnIds.get(j), value);
                  if (columnIds.get(j).equals(UNIQUE_ID_FIELD)) {
                    clientId = columnIds.get(j);
                  }
                  cellCount++;
                  updateProgress(cellCount, maxCells);
                }
              }
              if (!empty) {
                TemplateData rowData =
                    new TemplateData(template.get(), agency, clientId, rowDataMap);
                allTemplateData.add(rowData);
              }
            } else if (currentRow.getRowNum() == 1) {
              Iterator<Cell> cellIter = currentRow.cellIterator();

              // store column names
              while (cellIter.hasNext()) {
                Cell c = cellIter.next();
                String cellValue = df.formatCellValue(c);
                if (!EqualUtil.isEmpty(cellValue)) {
                  columnIds.add(cellValue);
                }
              }
            }
          }
          closeExcel(excelDoc);
        }
        return allTemplateData;
      }

      private Workbook loadExcel(String path) {
        File file = new File(path);
        Workbook wb;
        try {
          wb = WorkbookFactory.create(file);
        } catch (EncryptedDocumentException | IOException e) {
          e.printStackTrace();
          wb = null;
        }
        return wb;
      }

      private void closeExcel(Workbook workbook) {
        try {
          workbook.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }

      private int computeCellCount(Sheet sheet) {
        return (sheet.getLastRowNum() - 3) * (sheet.getRow(1).getLastCellNum() - 1);
      }
    };
  }
}
