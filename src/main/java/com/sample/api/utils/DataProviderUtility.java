package com.sample.api.restservice.utils;


public class DataProviderUtility {
	
	public static Object[][] getDataProviderWithIndex(Object[][] data){
		Object[][] modified_data = new Object[data.length][data[0].length+1];
		for(int i=0;i<modified_data.length;i++){
			int j;
			for(j=0;j<modified_data[0].length-1;j++){
				modified_data[i][j] = data[i][j];
			}
			modified_data[i][j] = new DataProviderIndex(i+1);
		}
		return modified_data;
	}
}
