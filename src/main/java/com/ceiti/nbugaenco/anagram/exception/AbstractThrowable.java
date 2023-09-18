package com.ceiti.nbugaenco.anagram.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractThrowable extends Throwable {

    private final String action;
    private final String message;
    private final HttpStatus status;
    private final Throwable cause;

}
