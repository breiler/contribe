package com.breiler.contribe.repository;

public enum StatusEnum {
    OK(0),
    NOT_IN_STOCK(1),
    DOES_NOT_EXIST(2);

    private final int statusCode;

    StatusEnum(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return this.statusCode;
    }
}
