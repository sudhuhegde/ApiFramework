package com.sample.api.models.users;

import lombok.Data;

@Data
public class UpdateUserResponse {

    private Integer code;
    private String type;
    private String message;

}
