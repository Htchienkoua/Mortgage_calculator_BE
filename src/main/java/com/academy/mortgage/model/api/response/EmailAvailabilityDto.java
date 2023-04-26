package com.academy.mortgage.model.api.response;

public class EmailAvailabilityDto {
    private boolean isAvailable;
    private String message;

    public EmailAvailabilityDto(boolean isAvailable, String message) {
        this.isAvailable = isAvailable;
        this.message = message;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}