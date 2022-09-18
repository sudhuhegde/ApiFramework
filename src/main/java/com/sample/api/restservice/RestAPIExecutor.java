package com.sample.api.restservice;

import java.util.HashMap;
import java.util.Map;

import com.sample.api.restservice.abstraction.APIExecutor;
import com.sample.api.restservice.abstraction.APIResponse;

import com.sample.api.restservice.logger.Log;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class RestAPIExecutor implements APIExecutor {
    private static Log LOG = new Log(RestAPIExecutor.class);
    private RequestSpecification requestSpecification;
    private APIResponse apiResponse;
    private Response response;

    public RestAPIExecutor() {
        this.requestSpecification = RestAssured.given().relaxedHTTPSValidation().contentType(ContentType.JSON).and().accept(ContentType.JSON);
        this.apiResponse = new RestAPIResponse();
    }


    public RestAPIExecutor(ContentType contentType, ContentType accept) {
        this.requestSpecification = RestAssured.given().relaxedHTTPSValidation().contentType(contentType).and().accept(accept);
        this.apiResponse = new RestAPIResponse();
    }


    public RestAPIExecutor withRequestBody(String request) {
        if (null != request)
            this.requestSpecification.body(request);
        this.apiResponse.setRequestBody(request);
        return this;
    }

    public RestAPIExecutor withRequestHeader(String headerName, String headerValue) {
        this.requestSpecification.header(headerName, headerValue);
        this.apiResponse.setRequestHeaders(headerName, headerValue);
        return this;
    }

    public RestAPIExecutor withRequestHeaders(Map<String, String> headers) {
        this.requestSpecification.headers(headers);
        this.apiResponse.setRequestHeaders(headers);
        return this;
    }

    public RestAPIExecutor withRequestParams(Map<String, String> requestParams) {
        this.requestSpecification.queryParams(requestParams);
        this.apiResponse.setRequestParams(requestParams);
        return this;
    }

    public RestAPIExecutor withRequestParam(String key, String value) {
        this.requestSpecification.queryParam(key, value);
        this.apiResponse.setRequestParams(key, value);
        return this;
    }

    public RestAPIExecutor withPathParams(Map<String, String> pathParams) {
        this.requestSpecification.pathParams(pathParams);
        return this;
    }

    public RestAPIExecutor withPathParam(String key, String value) {
        this.requestSpecification.pathParam(key, value);
        return this;
    }

    public APIResponse postAndBuild(String url) {
        LOG.info("POST Request URL" + url);
        this.response = this.requestSpecification.post(url);
        return build();
    }

    public APIResponse putAndBuild(String url) {
        LOG.info("PUT Request URL" + url);
        this.response = this.requestSpecification.put(url);
        return build();
    }

    public APIResponse getAndBuild(String url) {
        LOG.info("GET Request URL" + url);
        this.response = this.requestSpecification.get(url);
        return build();
    }

    public APIResponse deleteAndBuild(String url) {
        LOG.info("DELETE Request URL" + url);
        this.response = this.requestSpecification.delete(url);
        return build();
    }

    private APIResponse build() {
        this.apiResponse.setResponseBodyAsString(this.response.asString());
        Headers responseHeaders = this.response.getHeaders();
        Map<String, String> responseHeaderMap = new HashMap<String, String>();
        for (Header header : responseHeaders) {
            responseHeaderMap.put(header.getName(), header.getValue());
        }
        this.apiResponse.setResponseHeaders(responseHeaderMap);
        this.apiResponse.setResponseCode(this.response.getStatusCode());
        this.apiResponse.setResponseTime(this.response.getTime());
        printApiDetail(this.apiResponse);
        this.requestSpecification = RestAssured.given().relaxedHTTPSValidation().contentType(ContentType.JSON).and().accept(ContentType.JSON);
        APIResponse response = this.apiResponse;
        this.apiResponse = new RestAPIResponse();

        return response;
    }


    private void printApiDetail(APIResponse apiResponse) {
        RestAPIResponse response = (RestAPIResponse) apiResponse;
        LOG.info("Response Body " + response.getResponseBodyAsString());
        LOG.info("Request Headers " + response.getRequestHeaders());
        if (response.getRequestBody() != null)
            LOG.info("Request Body  " + response.getRequestBody().toString());
        if(response.getRequestParams()!=null){
            response.getRequestParams().entrySet().forEach( entry->LOG.info("Request params key "+ entry.getKey() + " value "+ entry.getValue()));

        }
    }

}
