package com.nineplusten.app.model;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.TableView;

public class Reports {
	
	private TableView<TemplateData> dataTable;
	
	private List<Double> childEntries = new ArrayList<Double>();
	private List<Double> youthEntries = new ArrayList<Double>();
	private List<Double> seniorEntries = new ArrayList<Double>();
	private List<Double> adultEntries = new ArrayList<Double>();
	
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
	
	
	  public double getTargetChildPercent() {
		  double total_rows = (Integer) getValuesforColumn("target_group_children_ind").get(0);
		  System.out.println("total rows" + total_rows);
		  List<String> target_child = (List<String>) getValuesforColumn("target_group_children_ind").get(1);
		  int i = 0;
		  int count_child = 0;
		  for (i = 0; i < target_child.size(); i++) {
			  if (target_child.get(i).equalsIgnoreCase("yes")) {
				  count_child += 1;
			  }
		  }
		  System.out.println("count child " + count_child);
		  double child_percent_decimal = count_child/total_rows;
		  double child_percent = child_percent_decimal * 100;
		  System.out.println("percent hin child " + child_percent_decimal);

		  return child_percent;
	  }
	  
	  public double getTargetYouthPercent() {
		  double total_rows = (Integer) getValuesforColumn("target_group_youth_ind").get(0);
		  List<String> target_youth = (List<String>) getValuesforColumn("target_group_youth_ind").get(1);
		  int i = 0;
		  int count_youth = 0;
		  for (i = 0; i < target_youth.size(); i++) {
			  if (target_youth.get(i).equalsIgnoreCase("yes")) {
				  count_youth += 1;
			  }
		  }
		  double youth_percent = (count_youth/total_rows)*100;
		  return youth_percent;
		  
	  }
	  public double getTargetSeniorPercent() {
		  double total_rows = (Integer) getValuesforColumn("target_group_senior_ind").get(0);
		  List<String> target_senior = (List<String>) getValuesforColumn("target_group_senior_ind").get(1);
		  int i = 0;
		  int count_senior = 0;
		  for (i = 0; i < target_senior.size(); i++) {
			  if (target_senior.get(i).equalsIgnoreCase("yes")) {
				  count_senior += 1;
			  }
		  }
		  double senior_percent = (count_senior/total_rows)*100;
		  return senior_percent;
		  
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
