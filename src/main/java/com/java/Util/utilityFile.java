package com.java.Util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class utilityFile {

	public static FileInputStream fi;
	public static FileOutputStream fo;
	public static XSSFWorkbook wb;
	public static XSSFSheet ws;
	public static XSSFRow row;
	public static XSSFCell cell;

	public static void setCellData(String xlfile, String xlsheet, int data)
			throws IOException {
		fi = new FileInputStream(xlfile);

		wb = new XSSFWorkbook(fi);
		ws = wb.getSheet(xlsheet);

		XSSFRow row = ws.getRow(0);

		if (row == null) {
			// Create the row if it does not exist
			row = ws.createRow(0);
		}

		XSSFCell cell = row.getCell(0);

		if (cell == null) {
			// Create the cell if it does not exist
			cell = row.createCell(0);
		}
		cell.setCellValue(data);
		fi.close();

		fo = new FileOutputStream(xlfile);

		wb.write(fo);
		wb.close();
		fo.close();

	}

}
