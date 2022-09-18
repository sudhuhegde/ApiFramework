package com.sample.api.models.pets;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Pet implements Cloneable{
    private Long id;
    private PetsCategory category;
    private String name;
    private List<String> photoUrls;
    private List<PetTag> tags;
    private String status;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
