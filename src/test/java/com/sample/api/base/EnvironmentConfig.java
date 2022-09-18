package com.sample.api.base;

import java.io.IOException;
import java.util.Properties;

public class EnvironmentConfig {

	private static EnvironmentConfig environmentConfig;
	// Test
	private Properties envProps;
	private String currentEnv;

	private EnvironmentConfig(Properties props) {
		envProps = props;
	}

	private EnvironmentConfig(Properties props, String env) {
		envProps = props;
		currentEnv = env;
	}

	public synchronized static void initEnvironment(String env) {
//		if (s_instance != null) {
//			throw new RuntimeException("Trying to reinitilize env with:" + env);
//		}
		Properties props = new Properties();
		try {
			props.load(EnvironmentConfig.class.getClassLoader()
					.getResourceAsStream("environment/" + env.toUpperCase() + "config.properties"));
		} catch (IOException e) {
			throw new RuntimeException("Unable intialize environment.", e);
		}
		environmentConfig = new EnvironmentConfig(props, env);
	}

	public String getCurrentEnvironment() {
		return currentEnv;
	}

	public boolean isPRODEnv() {
		return getCurrentEnvironment().equalsIgnoreCase("PROD");
	}

	public static EnvironmentConfig getInstance() {
		if (environmentConfig == null) {
			throw new RuntimeException("Environment not initialized.");
		}
		return environmentConfig;
	}


	/**
	 * Return the value for this key from environment specific properties.
	 *
	 * @param key
	 * @return
	 */
	public String getValueForKey(String key) {

		return envProps.getProperty(key);
	}

	/**
	 * Sets the value for the key passed.
	 * @param key
	 * @param value
	 */
	public void setValueForKey(String key,String value) {
		envProps.setProperty(key, value);
	}
}
