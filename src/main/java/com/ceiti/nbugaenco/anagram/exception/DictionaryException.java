package com.ceiti.nbugaenco.anagram.exception;

import org.springframework.http.HttpStatus;

public class DictionaryException extends AbstractThrowable {

    public DictionaryException(String action, String message, HttpStatus status, Throwable cause) {
        super(action, message, status, cause);
    }

}
