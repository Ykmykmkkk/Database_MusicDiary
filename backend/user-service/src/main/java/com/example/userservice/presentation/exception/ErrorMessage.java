package com.example.userservice.presentation.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class ErrorMessage {

    private List<String> errors;
    public ErrorMessage(List<String> errors){this.errors=errors;}
}
