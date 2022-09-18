package com.sample.api.tests;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.javafaker.Faker;
import com.sample.api.assertions.PetsAPIAssertions;
import com.sample.api.base.BaseTest;
import com.sample.api.enums.HeaderType;
import com.sample.api.helper.PetsAPIHelper;
import com.sample.api.models.pets.PetTag;
import com.sample.api.models.pets.Pet;
import com.sample.api.models.pets.PetsCategory;
import com.sample.api.restservice.abstraction.APIResponse;
import com.sample.api.restservice.utils.ExcelUtility;
import com.sample.api.restservice.utils.JsonUtility;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PetsTests extends BaseTest {

    private static final Object lock = new Object();
    ExcelUtility excelUtility;
    PetsAPIHelper petsAPIHelper;
    PetsAPIAssertions petsAPIAssertions;

    @BeforeClass(alwaysRun = true)
    public void setUp(ITestContext context) throws Exception {
        synchronized (lock) {
            excelUtility = new ExcelUtility();
            petsAPIHelper = new PetsAPIHelper();
            petsAPIAssertions = new PetsAPIAssertions();
        }
    }

    @DataProvider(name = "createPets", parallel = true)
    public Object[][] createPets(ITestContext context) throws IOException {
        String inputExcel = "TestData/petsTest.xls";
        return readDataFromExcel(excelUtility, inputExcel, "createPets", context);
    }

    @DataProvider(name = "getPetsByStatus", parallel = true)
    public Object[][] getPetsByStatus(ITestContext context) throws IOException {
        String inputExcel = "TestData/petsTest.xls";
        return readDataFromExcel(excelUtility, inputExcel, "getPetsByStatus", context);
    }

    @DataProvider(name = "updatePets", parallel = true)
    public Object[][] updatePets(ITestContext context) throws IOException {
        String inputExcel = "TestData/petsTest.xls";
        return readDataFromExcel(excelUtility, inputExcel, "updatePets", context);
    }

    private Pet getPets(String dataTypeName) throws IOException {

        if (dataTypeName.equalsIgnoreCase("Null") || dataTypeName.equalsIgnoreCase("NotExistingPet")) {
            return null;
        }
        Faker faker = new Faker();
        Pet pet = new Pet();

        pet.setId(faker.random().nextLong(100000));
        pet.setName(faker.funnyName().name());
        pet.setStatus("Available");
        pet.setPhotoUrls(Arrays.asList(faker.internet().url(), faker.internet().url()));
        PetsCategory petsCategory = new PetsCategory();
        petsCategory.setId(faker.random().nextInt(100000));
        petsCategory.setName(faker.animal().name());
        pet.setCategory(petsCategory);
        PetTag petTag1 = new PetTag();
        petTag1.setId(faker.random().nextInt(100000));
        petTag1.setName(faker.name().name());
        pet.setTags(Arrays.asList(petTag1));
        return getPet(pet, dataTypeName);
    }

    private Pet getPet(Pet pet, String dataTypeName) throws IOException {
        if (!dataTypeName.contains("_")) {
            return pet;
        }
        JsonNode jsonNode = JsonUtility.getJsonNodeFromObject(pet);
        int numberOfOps = dataTypeName.split("_").length;
        String optype = dataTypeName.split("_")[0];
        String objectType = dataTypeName.split("_")[numberOfOps - 1];
        if (numberOfOps > 2) {
            if (optype.equalsIgnoreCase("missing")) {
                if (jsonNode.findValue(dataTypeName.split("_")[1]).isArray()) {
                    ((ObjectNode) ((ArrayNode) jsonNode.findValue(dataTypeName.split("_")[1])).get(0)).remove(objectType);
                } else {
                    ((ObjectNode) jsonNode.findValue(dataTypeName.split("_")[1])).remove(objectType);
                }
            } else if (optype.equalsIgnoreCase("null")) {
                ((ObjectNode) jsonNode.findValue(dataTypeName.split("_")[1])).set(objectType, null);
            } else if (optype.equalsIgnoreCase("empty")) {
                ((ObjectNode) jsonNode.findValue(dataTypeName.split("_")[1])).set(objectType, JsonUtility.getJsonNode(""));
            }
        } else {
            if (optype.equalsIgnoreCase("missing")) {
                ((ObjectNode) jsonNode).remove(objectType);
            } else if (optype.equalsIgnoreCase("null")) {
                ((ObjectNode) jsonNode).set(objectType, null);
            } else if (optype.equalsIgnoreCase("empty")) {
                if (jsonNode.get(objectType).isArray()) {
                    ((ObjectNode) jsonNode).set(objectType, JsonUtility.getJsonNodeFromObject(new ArrayList<String>()));
                } else {
                    ((ObjectNode) jsonNode).set(objectType, JsonUtility.getJsonNode(""));
                }
            }
            else if(objectType.equalsIgnoreCase("status")){
                ((ObjectNode) jsonNode).set(objectType, JsonUtility.getJsonNodeFromObject(optype.toLowerCase()));
            }
        }
        return (Pet) JsonUtility.getObjectFromJsonNode(jsonNode, Pet.class);
    }

    private Pet getUpdatedPet(Pet pet, String fieldsToBeUpdated, String valuesToBeUpdated) throws CloneNotSupportedException {
        Faker faker = new Faker();
        Pet updatedPet = (Pet) pet.clone();
       String fields[]= fieldsToBeUpdated.split(",");
        String values[] = valuesToBeUpdated.split(",");
        JsonNode petNode = JsonUtility.getJsonNodeFromObject(updatedPet);
        for(int i=0;i<fields.length;i++){
            String value = values[i].equalsIgnoreCase("valid")?faker.ancient().god():values[i];

            if(petNode.get(fields[i]).isArray()){
                if(value.equalsIgnoreCase("add")){
                    //handle separately for photUrls
                }
                else if(value.equalsIgnoreCase("remove")) {
                    //handle separately for photUrls
                }
            }
            else {
                ((ObjectNode) petNode).set(fields[i], JsonUtility.getJsonNodeFromObject(value));
            }
        }
        return (Pet) JsonUtility.getObjectFromJsonNode(petNode,Pet.class);
    }

    @Test(dataProvider = "createPets", groups = {"createPets"})
    public void createPetsTest(String testId, String dataTypeName, String headerType, String
            expectedStatusCode, String errorMessage, ITestContext context) throws Exception {
        Pet pet = getPets(dataTypeName);
        APIResponse createPetResponse = petsAPIHelper.createPet(pet, HeaderType.valueOf(headerType));
        petsAPIAssertions.assertCreatePetResponse(createPetResponse, Integer.valueOf(expectedStatusCode), errorMessage, pet);

    }

    @Test(dataProvider = "updatePets", groups = {"updatePets"})
    public void updatePetsTest(String testId, String dataTypeName,String fieldsToBeUpdated,String valuesToBeUpdated, String headerType, String
            expectedStatusCode, String errorMessage, ITestContext context) throws Exception {
        Pet pet = getPets(dataTypeName);
        petsAPIHelper.createPet(pet, HeaderType.valueOf(headerType));//use db to create
        Pet updatePet = getUpdatedPet(pet,fieldsToBeUpdated,valuesToBeUpdated);
        APIResponse updatePetResponse = petsAPIHelper.updatePet(updatePet, HeaderType.valueOf(headerType));
        petsAPIAssertions.assertCreatePetResponse(updatePetResponse, Integer.valueOf(expectedStatusCode), errorMessage, updatePet);

    }



    @Test(dataProvider = "getPetsByStatus", groups = {"getPetsByStatus"})
    public void getPetsByStatusTest(String testId, String dataTypeName,String numberOfPets,String queryStatus, String headerType, String
            expectedStatusCode, String errorMessage, ITestContext context) throws Exception {
       List<Pet> petsCreated = new ArrayList<>();
       for(int i=0;i<Integer.valueOf(numberOfPets);i++) {
           Pet pet = getPets(dataTypeName);
           petsAPIHelper.createPet(pet, HeaderType.valueOf(headerType));//Use db to create
           petsCreated.add(pet);
       }
        APIResponse getPetByStatusResponse = petsAPIHelper.getPetByStatus(queryStatus, HeaderType.valueOf(headerType));
        petsAPIAssertions.assertGetPetResponse(getPetByStatusResponse, Integer.valueOf(expectedStatusCode),errorMessage,petsCreated);
    }


}
