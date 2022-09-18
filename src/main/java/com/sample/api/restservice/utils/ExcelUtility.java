package com.sample.api.restservice.utils;

import java.io.InputStream;


import com.sample.api.restservice.file.FileReader;
import com.sample.api.restservice.logger.Log;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

public class ExcelUtility{
	private static Log LOG = new Log(ExcelUtility.class);
	
	
	/**
	 * @param relativeExcelFile - The relative path to the excel file from current project directory.
	 * 
	 * Example: If your project name is ABC and your .xls file is in the directory com/sample/qa/testfiles 
	 * then relativeExcelFile should be "com/sample/qa/testfiles" 
	 * 
	 * Excel File format
	 * 
	 * <table border="1">
	 *	<tr ><td>startCell<td>          <td>           <td>         <td></tr>
	 *  <tr><td>       <td> data 1.1 <td> data 2.1  <td>         <td></tr>
	 *  <tr><td>       <td> data 1.2 <td> data 2.2  <td>         <td></tr>
	 *  <tr><td>       <td>          <td>           <td> endCell <td></tr>
	 * </table>
	 * 
	 * 
	 * @param sheetName - This represents the sheet name to be read from. 
	 * @param keyword - This represents the start/end keyword.
	 * 
	 * @return 2D data array
	 */

		public String[][] getTableArray(InputStream io, String sheetName, String startCell,String endCell){
		      String[][] tabArray=null;
		      try{
		    	  
		    	  
		    	  Workbook workbook = Workbook.getWorkbook(io);
		    	  Sheet sheet = workbook.getSheet(sheetName);          
		          int startRow,startCol, endRow, endCol,ci,cj;          
		          Cell tableStart=sheet.findCell(startCell);
		          startRow=tableStart.getRow();
		          startCol=tableStart.getColumn();
		          Cell tableEnd= sheet.findCell(endCell, startCol+1,startRow+1, 100, 64000,  false);                               

		          endRow=tableEnd.getRow();
		          endCol=tableEnd.getColumn();
		          LOG.info("startRow="+startRow+", endRow="+endRow+", " +
		                  "startCol="+startCol+", endCol="+endCol);
		          tabArray=new String[endRow-startRow-1][endCol-startCol-1];
		          ci=0;

		          for (int i=startRow+1;i<endRow;i++,ci++){
		              cj=0;
		              for (int j=startCol+1;j<endCol;j++,cj++){
		                  tabArray[ci][cj]=sheet.getCell(j,i).getContents();
		              }
		          }
		      }
		      catch (Exception e)    {
		    	  LOG.error("error in getTableArray()",e);
		      }
		      return(tabArray);
		  }
		

	}