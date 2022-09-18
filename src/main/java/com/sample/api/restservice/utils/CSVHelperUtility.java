package com.sample.api.restservice.utils;



import com.sample.api.restservice.logger.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CSVHelperUtility {

	private final Log s_logger  = new Log(CSVHelperUtility.class);




	public void createCSV(String fileName, String[] rows) throws IOException {
		System.out.println("\ncreating file "+fileName);
		FileWriter fw = new FileWriter(fileName);
		StringBuilder sb = new StringBuilder();
		for(int i =0;i<rows.length;i++) {
			sb.append(rows[i]);
			sb.append(',');

		}

		sb.append('\n');
		fw.write(sb.toString());
		fw.close();

	}

	public void addRows(String fileName , String[] rows) throws IOException {
		FileWriter fw = new FileWriter(fileName, true);
		StringBuilder sb = new StringBuilder();
		for(int i =0;i<rows.length;i++) {
			sb.append(rows[i]);
			sb.append(',');

		}
		sb.append('\n');
		fw.write(sb.toString());
		fw.close();
	}



	public ArrayList<String[]> readCSV (String csvFile) {

		String line = "";
		String csvSplitBy = ",";

		ArrayList<String[]> csvRows = new ArrayList<>();

		try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

			while ((line = br.readLine()) != null) {

				// use comma as separator
				String[] eachRow = line.split(csvSplitBy);
				csvRows.add(eachRow);



			}

		} catch (IOException e) {
			s_logger.error("exception caught "+e);
		}
		return csvRows;
	}

	public void mergeCSV(String file1 , String file2 , String newFile) throws IOException {
		List<String[]> file1List = readCSV(file1);
		List<String[]> file2List = readCSV(file2);


		List<String[]> newList = Stream.concat(file1List.stream(), file2List.stream())
				.collect(Collectors.toList());

		createCSV(newFile, newList.get(0));
		newList.stream().forEach(row -> {
			try {
				addRows(newFile, row);
			} catch (IOException e) {

				s_logger.error("exception caught "+e);
			}
		});


	}
}
