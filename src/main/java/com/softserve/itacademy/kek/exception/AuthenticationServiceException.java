package com.softserve.itacademy.kek.exception;


import com.softserve.itacademy.kek.services.impl.AuthenticationServiceImpl;

/**
 * Exception for {@link AuthenticationServiceImpl }
 */
public class AuthenticationServiceException extends ServiceException {

    public AuthenticationServiceException(String message, Exception ex) {
        super(message, ex);
    }

    public AuthenticationServiceException(String message, int errorCode) {
        super(message, errorCode);
    }
}
