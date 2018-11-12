package com.nineplusten.app.testingdata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.nineplusten.app.model.TemplateData;




public class Exceljson {

	private List<TemplateData> allTempData = new ArrayList<TemplateData>();
	public List<TemplateData> getExcelasJson(XSSFSheet sheet) {
  	  
  	  JsonArray sheetArray = new JsonArray();
      ArrayList<String> columnNames = new ArrayList<String>();
      Iterator<Row> sheetIterator = sheet.iterator();
      int tempcount = 0;
      while (sheetIterator.hasNext()) {
    	  Row currentRow = sheetIterator.next();
           JsonObject jsonObject = new JsonObject();
           
           if (currentRow.getRowNum() >= 3) {
        	   
        	   Map<String, String> rowData = new HashMap<String, String>();
        	   TemplateData newrow = new TemplateData(rowData);
        	   allTempData.add(newrow);
        	   
        	   for (int j = 0; j < columnNames.size(); j++) {
          		  if (currentRow.getCell(j) != null) {
          			  	//System.out.println(currentRow.getCell(j));
          			  	String value = currentRow.getCell(j).toString();
          			  	System.out.println(columnNames.get(j));
          			  	System.out.println(value);
          			  	//rowData.put(columnNames.get(j), value);
          			  	allTempData.get(tempcount).getFieldData().put(columnNames.get(j), value);
          			  	System.out.println("end of if statement");
                    } else {
                    	rowData.put(columnNames.get(j), "");
                    	
                    }
          		
          	   }
        	  
        	   tempcount = tempcount+1;
        	   	System.out.println(tempcount);
         		System.out.println("end of for loop");

        	  
            } else if (currentRow.getRowNum() == 1){
                // store column names
                for (int k = 0; k < currentRow.getPhysicalNumberOfCells(); k++) {
                	//System.out.println(currentRow.getCell(k).getStringCellValue());
                    columnNames.add(currentRow.getCell(k).getStringCellValue());
                }
            }
        }
      	//System.out.println(fieldData.size());
		return allTempData;

            
    }


}
