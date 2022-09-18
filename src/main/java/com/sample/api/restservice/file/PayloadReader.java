package com.sample.api.restservice.file;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;


public class PayloadReader {

    //method to get .properties file
    public Properties getPayloadPropertyFile(String filePath) throws IOException {
        InputStream io = FileReader.getInstance().readInputStreamFromClassPath(filePath);
        Properties p = new Properties();
        p.load(new InputStreamReader(io,"UTF-8"));
        return p;
    }
    //method to read property value from .properties file for specific scenario
    public String readPayloadPropertyFile(String filePath, String productId) throws IOException {
        InputStream io = FileReader.getInstance().readInputStreamFromClassPath(filePath);
        Properties p = new Properties();
        p.load(io);
        return p.getProperty(productId);
    }


}
