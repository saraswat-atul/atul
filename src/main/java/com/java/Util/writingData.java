package com.java.Util;

import java.io.IOException;

public class writingData {
	static String file_path = System.getProperty("user.dir") + ".\\ExcelFiles\\TestData.xlsx";

	public static void writePackage(int result) {

	
			try {
				utilityFile.setCellData(file_path, "Sheet1", result);
			} catch (IOException e) {

				e.printStackTrace();
			}
		
		System.out.println("Writing of Packages Done");
	}

}
