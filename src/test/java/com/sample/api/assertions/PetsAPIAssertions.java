package com.sample.api.assertions;

import com.sample.api.models.pets.CreatePetErrorResponse;
import com.sample.api.models.pets.Pet;
import com.sample.api.models.pets.PetTag;
import com.sample.api.restservice.abstraction.APIResponse;
import com.sample.api.restservice.utils.JsonUtility;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.testng.Assert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PetsAPIAssertions {
    public void assertCreatePetResponse(APIResponse createPetResponse, Integer expectedStatusCode, String errorMessage, Pet pet) throws IOException {
        Assert.assertEquals(createPetResponse.getResponseCode(), expectedStatusCode, "create pet status code not matching");

        if (expectedStatusCode != 200) {
            CreatePetErrorResponse actualResponse = (CreatePetErrorResponse) JsonUtility.getObjectFromString(createPetResponse.getResponseBodyAsString(), CreatePetErrorResponse.class);
            //assert error response
        } else {
            Pet actualPet = (Pet) JsonUtility.getObjectFromString(createPetResponse.getResponseBodyAsString(), Pet.class);
            if (pet.getId() == null) {
                actualPet.setId(null);
            }
            if(CollectionUtils.isEmpty(pet.getTags()) ){

            }
            else if(pet.getTags().get(0).getId()==null){
                pet.getTags().get(0).setId(0);
            }
            if(pet.getCategory()==null || pet.getCategory().getId()==null){
                pet.getCategory().setId(0);
            }
            Assert.assertEquals(actualPet, pet, "created and actual pet are not matching");
            //assert valid response using pet
        }
    }

    public void assertGetPetResponse(APIResponse getPetByStatusResponse, Integer expectedStatusCode, String errorMessage, List<Pet> petsCreated) throws IOException {
        Assert.assertEquals(getPetByStatusResponse.getResponseCode(), expectedStatusCode, "get pet status code not matching");

        List<Pet> getPetsActualResponse = ( List<Pet>) JsonUtility.getObjectFromString(getPetByStatusResponse.getResponseBodyAsString(), List.class);
       // Assert.assertTrue(equalLists(petsCreated,getPetsActualResponse));
    }

    public  boolean equalLists(List first, List second){
        if (first == null && second == null){
            return true;
        }
        if((first == null && second != null)
                || first != null && second == null
                || first.size() != second.size()){
            return false;
        }
        first = new ArrayList<>(first);
        second = new ArrayList<>(second);

        Collections.sort(first);
        Collections.sort(second);
        return first.equals(second);
    }
}
