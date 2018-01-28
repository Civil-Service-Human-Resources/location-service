package uk.gov.cshr.locationservice;

public class LocationServiceException extends Exception {

    private int errorCode;

    public LocationServiceException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
