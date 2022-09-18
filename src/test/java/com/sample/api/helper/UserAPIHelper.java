package com.sample.api.helper;

import com.sample.api.base.EnvironmentConfig;
import com.sample.api.constants.PropertiesKeysConstant;
import com.sample.api.enums.HeaderType;
import com.sample.api.models.users.User;
import com.sample.api.restservice.RestAPIExecutor;
import com.sample.api.restservice.abstraction.APIExecutor;
import com.sample.api.restservice.abstraction.APIResponse;
import com.sample.api.restservice.utils.JsonUtility;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserAPIHelper {
    private static String BASE_URL = null;
    private static String CREATE_USER = "/user/createWithArray";
    private static String UPDATE_USER = "/user/{username}";
    private static String GET_USER = "/user/{username}";

    public UserAPIHelper() {
        BASE_URL = EnvironmentConfig.getInstance().getValueForKey(PropertiesKeysConstant.PET_STORE_BASE_URL);
    }

    public APIResponse createUser(List<User> users, HeaderType headerType) {
        APIExecutor apiExecutor = new RestAPIExecutor();
        return apiExecutor.withRequestBody(JsonUtility.getStringFromObject(users)).withRequestHeaders(getUserAPIHeaders(headerType)).postAndBuild(BASE_URL+CREATE_USER);

    }


    public APIResponse updateUser(User user, String username, HeaderType headerType) {
        APIExecutor apiExecutor = new RestAPIExecutor();
        return apiExecutor.withRequestBody(JsonUtility.getStringFromObject(user)).withPathParam("username", username).withRequestHeaders(getUserAPIHeaders(headerType)).putAndBuild(BASE_URL+UPDATE_USER);


    }

    public APIResponse getUser(String username,HeaderType headerType) {
        APIExecutor apiExecutor = new RestAPIExecutor();
        return apiExecutor.withPathParam("username", username).withRequestHeaders(getUserAPIHeaders(headerType)).getAndBuild(BASE_URL+GET_USER);

    }

    private Map<String, String> getUserAPIHeaders(HeaderType headerType) {
        Map<String, String> headers = new HashMap<>();
        headers.put("accept", "application/json");
        headers.put("Content-Type", "application/json");
        switch (headerType) {

            case MISSING_CONTENT_TYPE:
                headers.remove("Content-Type");
                break;
            case INVALID_CONTENT_TYPE:
                headers.put("Content-Type", "INVALID");
                break;
            default://default means all valid
               break;
        }
        return headers;
    }

}
