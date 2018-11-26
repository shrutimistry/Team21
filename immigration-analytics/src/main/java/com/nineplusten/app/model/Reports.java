package com.nineplusten.app.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Years;
import org.joda.time.format.DateTimeFormat;

import javafx.scene.control.TableView;

public class Reports {
	
	private TableView<TemplateData> dataTable;
	
	private double childEntries;
	private double youthEntries;
	private double seniorEntries;
	private double adultEntries;
	private double totalRows;
	
	public Reports(TableView<TemplateData> dataTable) {
		this.dataTable = dataTable;
	}

	private List<Object> getValuesforColumn(String columnID) {
		  //final String COLUMN_ID = "client_validation_type_id";
		    List<Object> rowCount = new ArrayList<>();
		    List<String> values = new ArrayList<>();
		    int total = 0;
		    for (TemplateData row: this.dataTable.getItems()) {
		      values.add(row.getFieldData().get(columnID));
		      total += 1;
		    }
		    rowCount.add(total);
		    rowCount.add(values);
		    
		   return rowCount;
	}
	
	public void sortAges() {
		DateTime today = DateTime.now();
		
		this.totalRows = (Integer) getValuesforColumn("client_birth_dt").get(0);
		  System.out.println("total rows" + totalRows);
		  List<String> rowNums = (List<String>) getValuesforColumn("client_birth_dt").get(1);
		  int i = 0;
		  for (i = 0; i < rowNums.size(); i++) {
			  
			  String year = rowNums.get(i).substring(rowNums.get(i).length() - 2);
			  if (Integer.parseInt(year) <= 99 && Integer.parseInt(year) >= today.getYear()) {
				  year = "19" + year;
			  }
			  else {
				  year = "20" + year;
			  }
			  //DateTime clientBirthday = DateTime.parse(rowNums.get(i));
			
			 
			  //Years duration = Years.yearsBetween(today, clientBirthday);
			  
			  int difference = today.getYear() - Integer.parseInt(year);
			  
			  if (difference <= 14) {
				  this.childEntries++;
			  }
			  else if (difference > 14 && difference <= 24) {
				  this.youthEntries++;
			  }
			  else if (difference > 24 && difference <= 64) {
				  this.adultEntries++;
			  }
			  else {
				  this.seniorEntries++;
			  }
		  }
	}
	
	
	  public double getTargetChildPercent() {
		  /*double total_rows = (Integer) getValuesforColumn("target_group_children_ind").get(0);
		  System.out.println("total rows" + total_rows);
		  List<String> target_child = (List<String>) getValuesforColumn("target_group_children_ind").get(1);
		  int i = 0;
		  int count_child = 0;
		  for (i = 0; i < target_child.size(); i++) {
			  if (target_child.get(i).equalsIgnoreCase("yes")) {
				  count_child += 1;
			  }
		  }
		  System.out.println("count child " + count_child);*/
		  double childPercent = (childEntries/totalRows) * 100;
		  
		  return childPercent;
	  }
	  
	  public double getTargetYouthPercent() {
		  /*double total_rows = (Integer) getValuesforColumn("target_group_youth_ind").get(0);
		  List<String> target_youth = (List<String>) getValuesforColumn("target_group_youth_ind").get(1);
		  int i = 0;
		  int count_youth = 0;
		  for (i = 0; i < target_youth.size(); i++) {
			  if (target_youth.get(i).equalsIgnoreCase("yes")) {
				  count_youth += 1;
			  }
		  }
		  double youth_percent = (count_youth/total_rows)*100;
		  return youth_percent;*/
		  
		  double youthPercent = (youthEntries/this.totalRows)*100;
		  
		  return youthPercent;
		  
	  }
	  public double getTargetSeniorPercent() {
		  /*double total_rows = (Integer) getValuesforColumn("target_group_senior_ind").get(0);
		  List<String> target_senior = (List<String>) getValuesforColumn("target_group_senior_ind").get(1);
		  int i = 0;
		  int count_senior = 0;
		  for (i = 0; i < target_senior.size(); i++) {
			  if (target_senior.get(i).equalsIgnoreCase("yes")) {
				  count_senior += 1;
			  }
		  }
		  double senior_percent = (count_senior/total_rows)*100;
		  return senior_percent;*/
		  double seniorPercent = (seniorEntries/this.totalRows)*100;
		  
		  return seniorPercent;
		  
	  }
	  public double getNonEmptyCellCount(String columnid) {
		  List<String> cells_for_column = (List<String>) getValuesforColumn(columnid).get(1);
		  int total_rows = (Integer) getValuesforColumn(columnid).get(0);
		  double cell_count = 0;
		  for (int i = 0; i < total_rows; i++) {
			  if (cells_for_column.get(i) != null && !cells_for_column.get(i).isEmpty()) {
				  cell_count +=1;
				  
			  }
		  }
		  System.out.println("cellcount: " + cell_count);
		  return cell_count/total_rows;
		  
	  }
	  
}
