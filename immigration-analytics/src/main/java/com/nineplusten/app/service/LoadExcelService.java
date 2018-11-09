package com.nineplusten.app.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class LoadExcelService extends Service<List<String>> {
  private ReadOnlyStringProperty filePath;
  //**TEMPORARY ADDED A NEW CONSTRUCTOR **//
  private String filePath_string;

  public LoadExcelService(ReadOnlyStringProperty filePath) {
    this.filePath = filePath;
  }
  public LoadExcelService(String filePath_string) {
	    this.filePath = filePath;
  }

  @Override
  protected Task<List<String>> createTask() {
    return new Task<List<String>>() {
      @Override
      protected List<String> call() throws Exception {
        Workbook excelDoc = loadExcel(filePath.get());
        List<String> dataColumns = parseExcelSheet(excelDoc);
        closeExcel(excelDoc);
        return dataColumns;
      }

      protected List<String> parseExcelSheet(Workbook excelDoc) {
        // TODO may need to adjust parsing algorithm according to template formats
        List<String> columnList = null;
        Sheet dataSheet = excelDoc.getSheet("Data Fields");
        if (dataSheet != null) {
          columnList = parseDataFieldsSheet(dataSheet);
        } else {
          Sheet sheet = excelDoc.getSheetAt(0);
          columnList = parseSingleTemplate(sheet);
        }
        return columnList;
      }

      private List<String> parseSingleTemplate(Sheet sheet) {
        List<String> columnList = new ArrayList<String>();
        // Get row 3
        Row headerRow = sheet.getRow(2);
        DataFormatter df = new DataFormatter();
        Iterator<Cell> cellIter = headerRow.cellIterator();
        while(cellIter.hasNext()) {
          Cell c = cellIter.next();
          String cellVal = df.formatCellValue(c);
          if (!cellVal.trim().equals("")) {
            columnList.add(cellVal);
          }
        }
        return columnList;
      }

      private List<String> parseDataFieldsSheet(Sheet dataFieldsSheet) {
        final int SKIP = 3;
        DataFormatter df = new DataFormatter();
        List<String> columnList = new ArrayList<String>();
        Iterator<Row> rowIter = dataFieldsSheet.rowIterator();

        // Skip rows
        for (int i = 0; i < SKIP; i++) {
          rowIter.next();
        }

        while (rowIter.hasNext()) {
          Row row = rowIter.next();
          String cellVal = df.formatCellValue(row.getCell(0));
          if (!cellVal.trim().equals("")) {
            columnList.add(cellVal);
          }
        }
        return columnList;
      }

      public Workbook loadExcel(String path) {
        File file = new File(path);
        Workbook wb;
        try {
          wb = WorkbookFactory.create(file);
        } catch (EncryptedDocumentException | InvalidFormatException | IOException e) {
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
    };
  }
}
