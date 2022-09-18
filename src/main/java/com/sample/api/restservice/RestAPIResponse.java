package com.sample.api.restservice;

import com.sample.api.restservice.abstraction.APIResponse;

import io.restassured.response.Response;

public class RestAPIResponse extends APIResponse {

	private Response response;
	
	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	} 
}
