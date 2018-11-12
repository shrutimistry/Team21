package com.nineplusten.app.testingdata;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.gson.JsonArray;
import com.mashape.unirest.http.ObjectMapper;
import com.nineplusten.app.model.TemplateData;

public class Main {
	
	public static void main(String[] args) throws IOException{
		
		FileInputStream fs = new FileInputStream("C:\\Users\\Shruti2\\Downloads\\testex.xlsx");
		XSSFWorkbook wb = new XSSFWorkbook(fs);
		XSSFSheet sheet = wb.getSheetAt(0);
		Exceljson printjson = new Exceljson();
		
		List<TemplateData> alldata = new ArrayList<TemplateData>();
		alldata = printjson.getExcelasJson(sheet);
		for (String key : ((alldata.get(0)).getFieldData()).keySet()) {
			System.out.println(key + ":" + (alldata.get(0).getFieldData()).get(key));
		}
		System.out.print(alldata.size());
		
	
	}
}

