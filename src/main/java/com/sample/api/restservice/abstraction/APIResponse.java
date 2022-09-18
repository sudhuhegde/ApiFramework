package com.sample.api.restservice.abstraction;

import java.util.HashMap;
import java.util.Map;

public abstract class  APIResponse {

	private Map<String, String> responseHeaders;
	private Integer  responseCode;
	private String responseBodyAsString;
	private Object requestBody;
	private Map<String, String> requestHeaders;
	private Map<String,String> requestParams;
	private Map<String,String> pathParams;
	private long responseTime;

	public long getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(long responseTime) {
		this.responseTime = responseTime;
	}

	public Map<String, String> getPathParams() {
		return pathParams;
	}

	public void setPathParams(Map<String, String> pathParams) {
		if(this.pathParams==null) {
			this.pathParams=new HashMap<String, String>();
		}
		this.pathParams.putAll(pathParams);
	}
	public void setPathParams(String key, String value) {
		if(this.pathParams==null) {
			this.pathParams=new HashMap<String, String>();
		}
		this.pathParams.put(key,value);
	}

	public Map<String, String> getRequestParams() {
		return requestParams;
	}

	public void setRequestParams(Map<String, String> requestParams) {
		if(this.requestParams==null) {
			this.requestParams=new HashMap<String, String>();
		}
		this.requestParams.putAll(requestParams);
	}
	public void setRequestParams(String key, String value) {
		if(this.requestParams==null) {
			this.requestParams=new HashMap<String, String>();
		}
		this.requestParams.put(key, value);
	}



	public Map<String, String> getRequestHeaders() {
		return requestHeaders;
	}

	public void setRequestHeaders(Map<String, String> requestHeaders) {
		if(this.requestHeaders == null) {
			this.requestHeaders = new HashMap<String, String>();
		}
		this.requestHeaders.putAll(requestHeaders);
	}
	public void setRequestHeaders(String headerName, String headerValue ) {

		if(this.requestHeaders == null) {
			this.requestHeaders = new HashMap<String, String>();
		}
		this.requestHeaders.put(headerName, headerValue);
	}
	public Object getRequestBody() {
		return requestBody;
	}

	public void setRequestBody(Object requestBody) {
		this.requestBody = requestBody;
	}

	public Map<String, String> getResponseHeaders() {
		return responseHeaders;
	}

	public void setResponseHeaders(Map<String, String> responseHeaders) {
		this.responseHeaders = responseHeaders;
	}

	public Integer getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(Integer responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseBodyAsString() {
		return responseBodyAsString;
	}

	public void setResponseBodyAsString(String responseBodyAsString) {
		this.responseBodyAsString = responseBodyAsString;
	}


}
