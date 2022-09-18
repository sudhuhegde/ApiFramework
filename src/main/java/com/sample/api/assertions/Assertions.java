package com.sample.api.restservice.assertions;

public interface Assertions {

    void equals(String actual, String expected);
    void equals(Boolean actual, Boolean expected);
    void equals(Object actual, Object expected);

}
