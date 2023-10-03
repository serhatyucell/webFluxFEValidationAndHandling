package com.serhat.reactive.validationDemo.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

public class BadRequestException extends RuntimeException{
    private List<String> errorList;

    public BadRequestException(List<String> errorList) {
        this.errorList = errorList;
    }

    public BadRequestException(String errorMessage) {
        this.errorList = Arrays.asList(errorMessage);
    }

    public List<String> getErrorList(){
        return this.errorList;
    }

}
