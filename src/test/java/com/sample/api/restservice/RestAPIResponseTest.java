package com.sample.api.restservice;

import com.sample.api.restservice.abstraction.APIResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.*;

public class RestAPIResponseTest {

    @Mock
    APIResponse apiResponse = new RestAPIResponse();

    @Before
    public void setup(){
        apiResponse.setResponseCode(200);
    }

    @Test
    public void testResponseCode(){
        Assert.assertEquals(Integer.valueOf(200),apiResponse.getResponseCode());
    }

}