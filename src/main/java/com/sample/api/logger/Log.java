package com.sample.api.restservice.logger;

import java.util.Formatter;

import org.apache.log4j.Logger;



public class Log {

	static Logger log;

	
	public Log(Class<?> clazz){
		log = Logger.getLogger(clazz);
	}

	public void info(String msg) {
		log.info(msg);
	}


	public void error(String msg) {
		log.error(msg);

	}
	public void error(String msg,Throwable t) {
		log.error(msg,t);

	}

}
