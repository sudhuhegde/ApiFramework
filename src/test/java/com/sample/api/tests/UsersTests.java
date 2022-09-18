package com.sample.api.tests;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.javafaker.Faker;
import com.sample.api.assertions.UsersAPIAssertions;
import com.sample.api.base.BaseTest;
import com.sample.api.enums.HeaderType;
import com.sample.api.helper.UserAPIHelper;
import com.sample.api.models.users.User;
import com.sample.api.restservice.abstraction.APIResponse;
import com.sample.api.restservice.utils.ExcelUtility;
import com.sample.api.restservice.utils.JsonUtility;
import org.apache.commons.collections4.CollectionUtils;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UsersTests extends BaseTest {
    private static final Object lock = new Object();
    ExcelUtility excelUtility;
    UserAPIHelper userAPIHelper;

    UsersAPIAssertions usersAPIAssertions;

    @BeforeClass(alwaysRun = true)
    public void setUp(ITestContext context) throws Exception {
        synchronized (lock) {
            excelUtility = new ExcelUtility();
            userAPIHelper = new UserAPIHelper();
            usersAPIAssertions = new UsersAPIAssertions();
        }

    }

    @DataProvider(name = "createUsers",parallel = true)
    public Object[][] createUsers(ITestContext context) throws IOException {
        String inputExcel = "TestData/usersTest.xls";
        return readDataFromExcel(excelUtility, inputExcel, "createUsers", context);
    }

    @DataProvider(name = "getUsers",parallel = true)
    public Object[][] getUsers(ITestContext context) throws IOException {
        String inputExcel = "TestData/usersTest.xls";
        return readDataFromExcel(excelUtility, inputExcel, "getUsers", context);
    }

    @DataProvider(name = "updateUsers",parallel = true)
    public Object[][] updateUsers(ITestContext context) throws IOException {
        String inputExcel = "TestData/usersTest.xls";
        return readDataFromExcel(excelUtility, inputExcel, "updateUsers", context);
    }

    private List<User> getUsers(String dataTypeName, int numberOfUsers) throws IOException {

        if (dataTypeName.equalsIgnoreCase("Null") || dataTypeName.equalsIgnoreCase("NotExistingUser")) {
            return null;
        }

        List<User> userList = new ArrayList<>();
        Faker faker = new Faker();
        for (int i = 0; i < numberOfUsers; i++) {
            User user = new User();
            user.setId(faker.random().nextInt(10000000));
            user.setUserStatus(1);
            user.setEmail(faker.internet().emailAddress());
            user.setFirstName(faker.name().firstName());
            user.setLastName(faker.name().lastName());
            user.setUsername(faker.name().username());
            user.setPassword(faker.internet().password());
            user.setPhone(faker.phoneNumber().phoneNumber());
            user = getUser(user, dataTypeName);
            userList.add(user);

        }

        if (dataTypeName.contains("&")) {
            User user = getUser(userList.get(0), dataTypeName.split("&")[0]);
            userList.set(0, user);
        }
        return userList;
    }

    private boolean isValidUser(User user) {
        if (null == user) {
            return false;
        }
        //check emal,username,id are present
        return true;
    }

    private User getUser(User user, String dataTypeName) throws IOException {
        if (!dataTypeName.contains("_") || dataTypeName.contains("&")) {
            return user;
        }
        JsonNode jsonNode = JsonUtility.getJsonNodeFromObject(user);

        String optype = dataTypeName.split("_")[0];
        String objectType = dataTypeName.split("_")[1];

        if (optype.equalsIgnoreCase("missing")) {
            ((ObjectNode) jsonNode).remove(objectType);
        } else if (optype.equalsIgnoreCase("null")) {
            ((ObjectNode) jsonNode).set(objectType, null);
        } else if (optype.equalsIgnoreCase("invalid")) {
            ((ObjectNode) jsonNode).set(objectType, JsonUtility.getJsonNode("invalid"));
        } else if (optype.equalsIgnoreCase("empty")) {
            ((ObjectNode) jsonNode).set(objectType, JsonUtility.getJsonNode(""));
        }
        return (User) JsonUtility.getObjectFromJsonNode(jsonNode, User.class);
    }


    @Test(dataProvider = "createUsers", groups = {"createUsers"})
    public void createUsersTest(String testId, String dataTypeName, String numberOfUsers, String headerType, String
            expectedStatusCode, String errorMessage, ITestContext context) throws Exception {

        List<User> users = getUsers(dataTypeName, Integer.valueOf(numberOfUsers));
        APIResponse createApiResponse = userAPIHelper.createUser(users, HeaderType.valueOf(headerType));
        usersAPIAssertions.assertCreateApiResponse(createApiResponse, Integer.valueOf(expectedStatusCode));
        for (User user : users) {
            //Ideal way isto check in db
            APIResponse getUserAPIResponse = userAPIHelper.getUser(user.getUsername(), HeaderType.valueOf(headerType));
            int expectedGetResponseCode = isValidUser(user) ? 200 : 404;
            usersAPIAssertions.assertGetUserAPIResponse(getUserAPIResponse, expectedGetResponseCode, user, isValidUser(user));
        }

    }

    @Test(dataProvider = "getUsers", groups = {"getUsers"})
    public void getUsersTest(String testId, String dataTypeName, String numberOfUsers, String headerType, String
            expectedStatusCode, String errorMessage, ITestContext context) throws Exception {
        List<User> users = getUsers(dataTypeName, Integer.valueOf(numberOfUsers));

        if (CollectionUtils.isEmpty(users)) {
            APIResponse getUserAPIResponse = userAPIHelper.getUser("Inavaidd838dhhjdsjhsdhjdsj", HeaderType.valueOf(headerType));
            usersAPIAssertions.assertGetUserAPIResponse(getUserAPIResponse, Integer.valueOf(expectedStatusCode), null, isValidUser(null));
        } else {
            userAPIHelper.createUser(users, HeaderType.valueOf(headerType));
            APIResponse getUserAPIResponse = userAPIHelper.getUser(users.get(0).getUsername(), HeaderType.valueOf(headerType));
            usersAPIAssertions.assertGetUserAPIResponse(getUserAPIResponse, Integer.valueOf(expectedStatusCode), users.get(0), isValidUser(users.get(0)));
        }
        ;


    }

    @Test(dataProvider = "getUsers", groups = {"updateUsers"})
    public void updateUsersTest(String testId, String dataTypeName, String numberOfUsers, String headerType, String
            expectedStatusCode, String errorMessage, ITestContext context) throws Exception {

        List<User> users = getUsers(dataTypeName, Integer.valueOf(numberOfUsers));
        if (CollectionUtils.isEmpty(users)) {
            users = getUsers("ValidUser", 1);
            APIResponse updateUserApiResponse = userAPIHelper.updateUser(users.get(0), users.get(0).getUsername(), HeaderType.valueOf(headerType));
            usersAPIAssertions.assertUpdateUserAPIResponse(updateUserApiResponse, Integer.valueOf(expectedStatusCode), null, isValidUser(null));

        } else {
            userAPIHelper.createUser(users, HeaderType.valueOf(headerType));
            APIResponse updateUserApiResponse = userAPIHelper.updateUser(users.get(0), users.get(0).getUsername(), HeaderType.valueOf(headerType));
            usersAPIAssertions.assertUpdateUserAPIResponse(updateUserApiResponse, Integer.valueOf(expectedStatusCode), users.get(0), isValidUser(users.get(0)));
            APIResponse getUserAPIResponse = userAPIHelper.getUser(users.get(0).getUsername(), HeaderType.valueOf(headerType));
            usersAPIAssertions.assertUpdateUserAPIResponse(getUserAPIResponse, Integer.valueOf(expectedStatusCode), users.get(0), isValidUser(users.get(0)));
        }


    }
}
