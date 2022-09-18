package com.sample.api.base;

import com.sample.api.restservice.logger.Log;
import com.sample.api.restservice.utils.ExcelUtility;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;

import java.io.InputStream;

public class BaseTest {

    private static Log LOG = new Log(BaseTest.class);





    private void initEnvironment(String env) {
        LOG.info("Initilizing environment  " + env);
        EnvironmentConfig.initEnvironment(env);
    }

    @BeforeSuite(alwaysRun = true)
    @Parameters({"test.env"})
    public void initSuite(String env, ITestContext context) throws Exception {
        initEnvironment(env);

    }

    @AfterMethod(alwaysRun = true)
    public void afterEachMethod(ITestResult testResult) {

    }


    @AfterSuite(alwaysRun = true)
    @Parameters({"test.env"})
    public void afterSuite(String env) {

    }

    protected String[][] readDataFromExcel(ExcelUtility excelUtility, String fileName, String sheetName, ITestContext context) {

        InputStream io = getClass().getClassLoader().getResourceAsStream(fileName);
        String[][] data = excelUtility.getTableArray(io, sheetName, "StartTest", "EndTest");
        return data;
    }

    protected String getScenarioIdMessage(ITestContext context, String scenarioId) {
        return context.getName() + "_" + scenarioId;
    }
}
