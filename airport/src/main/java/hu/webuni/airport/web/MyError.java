package hu.webuni.airport.web;

import org.springframework.validation.FieldError;

import java.util.List;

public class MyError {
    private String message;
    private int errorCode;
    private List<FieldError> fieldErrors;

    public List<FieldError> getFieldErrors() {
        return fieldErrors;
    }

    public void setFieldErrors(List<FieldError> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public MyError(String message, int errorCode) {
        // super();
        this.message = message;
        this.errorCode = errorCode;
    }
}
