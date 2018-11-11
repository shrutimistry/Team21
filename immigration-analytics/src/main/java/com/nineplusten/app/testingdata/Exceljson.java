package com.nineplusten.app.testingdata;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Exceljson {
	JsonArray getExcelasJson(XSSFSheet sheet) {
  	  JsonObject sheetsJsonObject = new JsonObject();
  	  
  	  JsonArray sheetArray = new JsonArray();
        ArrayList<String> columnNames = new ArrayList<String>();
        Iterator<Row> sheetIterator = sheet.iterator();
        
        while (sheetIterator.hasNext()) {

            Row currentRow = sheetIterator.next();
            JsonObject jsonObject = new JsonObject();
            if (currentRow.getRowNum() != 0) {
          	  for (int j = 0; j < columnNames.size(); j++) {
          		  if (currentRow.getCell(j) != null) {
          			  	System.out.println(currentRow.getCell(j));
                        if (currentRow.getCell(j).getCellTypeEnum() == CellType.STRING) {
                            jsonObject.addProperty(columnNames.get(j), currentRow.getCell(j).getStringCellValue());
                        } else if (currentRow.getCell(j).getCellTypeEnum() == CellType.NUMERIC) {
                            jsonObject.addProperty(columnNames.get(j), currentRow.getCell(j).getNumericCellValue());
                        } else if (currentRow.getCell(j).getCellTypeEnum() == CellType.BOOLEAN) {
                            jsonObject.addProperty(columnNames.get(j), currentRow.getCell(j).getBooleanCellValue());
                        } else if (currentRow.getCell(j).getCellTypeEnum() == CellType.BLANK) {
                            jsonObject.addProperty(columnNames.get(j), "");
                        }
                    } else {
                        jsonObject.addProperty(columnNames.get(j), "");
                    }
          	  }
            } else {
                // store column names
                for (int k = 0; k < currentRow.getPhysicalNumberOfCells(); k++) {
                    columnNames.add(currentRow.getCell(k).getStringCellValue());
                }
            }
        }
		return sheetArray;

            
    }


}
