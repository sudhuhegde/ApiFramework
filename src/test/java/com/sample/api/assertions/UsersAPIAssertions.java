package com.sample.api.assertions;

import com.sample.api.models.users.CreateUserResponse;
import com.sample.api.models.users.User;
import com.sample.api.restservice.abstraction.APIResponse;
import com.sample.api.restservice.utils.JsonUtility;
import org.testng.Assert;

import java.io.IOException;

public class UsersAPIAssertions {

    public void assertCreateApiResponse(APIResponse response, Integer expectedResponseCode) throws IOException {
        Assert.assertEquals(response.getResponseCode(),expectedResponseCode,"response code not matching for create user");
        CreateUserResponse expected = null;
        if(expectedResponseCode==200){
            //form expected response
        }
        else{
            //form error response
        }

      //  Assert.assertEquals(JsonUtility.getObjectFromString(response.getResponseBodyAsString(),CreateUserResponse.class),expected,"response code not matching");
    }

    public void assertGetUserAPIResponse(APIResponse response,Integer expectedResponseCode, User user, boolean validUser) {

        Assert.assertEquals(response.getResponseCode(),expectedResponseCode,"response code not matching for get api ");
        if(expectedResponseCode==200){

        }
        else{

        }

    }

    public void assertUpdateUserAPIResponse(APIResponse response,Integer expectedResponseCode, User user, boolean validUser) {
        Assert.assertEquals(response.getResponseCode(),expectedResponseCode,"response code not matching for get api ");
    }
}
