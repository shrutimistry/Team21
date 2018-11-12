package com.nineplusten.app.testingdata;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;




public class Exceljson {

	private Map<String, String> fieldData;
	public JsonArray getExcelasJson(XSSFSheet sheet) {
  	  
  	  JsonArray sheetArray = new JsonArray();
      ArrayList<String> columnNames = new ArrayList<String>();
      Iterator<Row> sheetIterator = sheet.iterator();
      while (sheetIterator.hasNext()) {
    	  Row currentRow = sheetIterator.next();
           JsonObject jsonObject = new JsonObject();
           if (currentRow.getRowNum() >= 3) {
          	 for (int j = 0; j < columnNames.size(); j++) {
          		  if (currentRow.getCell(j) != null) {
          			  	System.out.println(currentRow.getCell(j));
          			  	String value = currentRow.getCell(j).toString();
          			  	System.out.println(columnNames.get(j));
          			  	fieldData.put(columnNames.get(j), value);
                    } else {
                        fieldData.put(columnNames.get(j), "");
                    }
          	  }
            }
        }
		return sheetArray;

            
    }


}
