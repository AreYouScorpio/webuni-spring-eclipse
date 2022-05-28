package hu.webuni.airport.web;

public class MyError {
    private String message;
    private int errorCode;

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
