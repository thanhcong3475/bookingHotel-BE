package com.cong.bookinghotel.exceptions;

public class RoleAlreadyExistException extends RuntimeException{
    public RoleAlreadyExistException(String message) {
        super(message);
    }
}
