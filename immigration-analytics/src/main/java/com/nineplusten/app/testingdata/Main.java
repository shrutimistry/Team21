package com.nineplusten.app.testingdata;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.gson.JsonArray;
import com.mashape.unirest.http.ObjectMapper;

public class Main {
	
	public static void main(String[] args) throws IOException{
		
		FileInputStream fs = new FileInputStream("C:\\Users\\Shruti2\\Downloads\\iCARE_template.xlsx");
		XSSFWorkbook wb = new XSSFWorkbook(fs);
		XSSFSheet sheet = wb.getSheetAt(0);
		Exceljson printjson = new Exceljson();
		

		
		JsonArray arrjson = printjson.getExcelasJson(sheet);
//		System.out.print(arrjson);
		
	
	}
}

