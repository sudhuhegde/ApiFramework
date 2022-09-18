package com.sample.api.restservice.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import com.sample.api.restservice.logger.Log;
import org.apache.commons.io.IOUtils;





/**
 * Reads file from given path and returns string.
 * 
 * @author s0h01rm
 */
public class FileReader {
	private static Log LOG = new Log(FileReader.class);



	private static final FileReader s_instance = new FileReader();

	public static FileReader getInstance() {
		return s_instance;
	}

	/**
	 * Reads a resource file from class path and returns the content as string.
	 * 
	 * @param path
	 * @return
	 */
	public String readFileFromClassPath(String path) {
		InputStream st = readInputStreamFromClassPath(path);
		return convertInputStreamToString(st);	
	}

	/**
	 * Convert the input stream to String.
	 * 
	 * @param stream
	 * @return
	 */
	private String convertInputStreamToString(InputStream stream) {
		StringWriter wr = new StringWriter();
		try {
			IOUtils.copy(stream, wr);
		} catch (IOException e) {
			LOG.error("Error reading file.", e);
		}
		return wr.toString();
	}

	/**
	 * Reads a resource file from class path and returns the content as InputStream.
	 * 
	 * @param path
	 * @return
	 * @throws FileNotFoundException 
	 */
	public InputStream readInputStreamFromClassPath(String path) {
		InputStream st = null;
				try {

					st=getClass().getClassLoader().getResourceAsStream(path);
					if(st==null) {

						st= new FileInputStream(path);
					} 
				}
		catch (FileNotFoundException e) {
			LOG.error("error while getting input stream ", e);
		}
		return st;
	}

	/**
	 * Read file from disk with absolute path and return file content as string.
	 * 
	 * @param absPath
	 * @return String fileContent
	 */
	public String readFileFromDisk(String absPath) {
		return readFileFromDisk(new File(absPath));
	}

	/**
	 * Read file and return file content as string.
	 * 
	 * @param file
	 * @return String fileContent
	 */
	public String readFileFromDisk(File file) {
		return convertInputStreamToString(readInputStreamFromDisk(file));
	}

	/**
	 * Read file from disk with absolute path and return file content as input stream.
	 * 
	 * @param absPath
	 * @return InputStream inputStream
	 */
	public InputStream readInputStreamFromDisk(String absPath) {
		return readInputStreamFromDisk(new File(absPath));

	}

	/**
	 * Read file and return the content as input stream
	 * 
	 * @param file
	 * @return InputStream inputStream
	 */
	public InputStream readInputStreamFromDisk(File file) {
		LOG.info("Reading file at location: "+ file.getAbsoluteFile());
		try {
			FileInputStream fis = new FileInputStream(file);
			LOG.info("File is read successfully.");
			return fis;
		} catch (FileNotFoundException e) {
			LOG.error("Error reading file.", e);
		}
		return null;

	}

}
