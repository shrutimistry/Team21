package com.nineplusten.app.model;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import javafx.scene.control.TableView;

public class Reports {

	private TableView<TemplateData> dataTable;
	private int totalRows;
	private double childEntries;
	private double youthEntries;
	private double adultEntries;
	private double seniorEntries;

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
		List<String> rowNums = (List<String>) getValuesforColumn("client_birth_dt").get(1);
		int i = 0;
		for (i = 0; i < rowNums.size(); i++) {

			String year = rowNums.get(i).substring(rowNums.get(i).length() - 2);
			//System.out.println(today.getYear());

			if (Integer.parseInt(year) <= 99 && Integer.parseInt(year) >= (today.getYear() - 2000)) {
				year = "19" + year;
			}
			else {
				year = "20" + year;
			}
			int difference = today.getYear() - Integer.parseInt(year);
			//System.out.println(difference);
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
		double childPercent = (this.childEntries/this.totalRows) * 100;
		return childPercent;
	}

	public double getTargetYouthPercent() {
		double youthPercent = (this.youthEntries/this.totalRows) * 100;
		return youthPercent;

	}

	public double getTargetSeniorPercent() {
		double seniorPercent = (this.seniorEntries/this.totalRows) * 100;
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
