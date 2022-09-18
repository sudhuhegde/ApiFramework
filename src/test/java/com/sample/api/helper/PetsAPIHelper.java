package com.sample.api.helper;

import com.sample.api.base.EnvironmentConfig;
import com.sample.api.constants.PropertiesKeysConstant;
import com.sample.api.enums.HeaderType;
import com.sample.api.models.pets.Pet;
import com.sample.api.restservice.RestAPIExecutor;
import com.sample.api.restservice.abstraction.APIExecutor;
import com.sample.api.restservice.abstraction.APIResponse;
import com.sample.api.restservice.utils.JsonUtility;

import java.util.HashMap;
import java.util.Map;

public class PetsAPIHelper {

    private static String BASE_URL = null;
    private static String CREATE_PET = "/pet";
    private static String UPDATE_PET = "/pet";
    private static String GET_PET_BY_STATUS= "/pet/findByStatus";

    public PetsAPIHelper() {
        BASE_URL = EnvironmentConfig.getInstance().getValueForKey(PropertiesKeysConstant.PET_STORE_BASE_URL);
    }
    public APIResponse createPet(Pet pet, HeaderType headerType) {
        APIExecutor apiExecutor = new RestAPIExecutor();
        return apiExecutor.withRequestBody(JsonUtility.getStringFromObject(pet)).withRequestHeaders(getPetsAPIHeaders(headerType)).postAndBuild(BASE_URL+CREATE_PET);

    }
    public APIResponse updatePet(Pet pet, HeaderType headerType) {
        APIExecutor apiExecutor = new RestAPIExecutor();
        return apiExecutor.withRequestBody(JsonUtility.getStringFromObject(pet)).withRequestHeaders(getPetsAPIHeaders(headerType)).putAndBuild(BASE_URL+UPDATE_PET);

    }
    public APIResponse getPetByStatus(String status, HeaderType headerType) {
        APIExecutor apiExecutor = new RestAPIExecutor();
        return apiExecutor.withRequestHeaders(getPetsAPIHeaders(headerType)).withRequestParam("status",status).getAndBuild(BASE_URL+GET_PET_BY_STATUS);

    }

    private Map<String, String> getPetsAPIHeaders(HeaderType headerType) {
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
