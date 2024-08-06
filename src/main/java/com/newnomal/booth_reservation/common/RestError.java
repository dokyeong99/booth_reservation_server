package com.newnomal.booth_reservation.common;

//에러를 일정한 형식으로 반환하는 클래스
public class RestError {
    private String id;
    private String message;

    public RestError(String id, String message) {
        this.id = id;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }
}