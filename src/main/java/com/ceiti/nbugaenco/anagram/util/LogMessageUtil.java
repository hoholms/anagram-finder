package com.ceiti.nbugaenco.anagram.util;

import lombok.experimental.UtilityClass;
import org.slf4j.Logger;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

@UtilityClass
public class LogMessageUtil {

    private static final String INFO_MESSAGE = "ACTION: {}, MESSAGE: {}, PAYLOAD: {}";
    private static final String ERROR_MESSAGE = "ACTION: {}, MESSAGE {}, ERROR: {}, PAYLOAD: {}";
    private static final String REQUEST_MESSAGE = "ACTION: {}, METHOD: {}, ENDPOINT: {}, MESSAGE {}, REQUEST: {}";
    private static final String RESPONSE_MESSAGE = "ACTION: {}, METHOD: {}, ENDPOINT: {}, STATUS{}, MESSAGE {}, RESPONSE: {}";

    public static void logInfo(Logger log, String action, String message, Object payload) {
        log.info(INFO_MESSAGE, action, message, payload);
    }

    public static void logError(Logger log, String action, String message, Throwable error, Object payload) {
        log.error(ERROR_MESSAGE, action, message, error.getMessage(), payload);
    }

    public static void logRequest(Logger log, String action, HttpMethod method, String endpoint, String message, Object request) {
        log.info(REQUEST_MESSAGE, action, method, endpoint, message, request);
    }

    public static void logResponse(Logger log, String action, HttpMethod method, String endpoint, HttpStatus status, String message, Object response) {
        log.info(RESPONSE_MESSAGE, action, method, endpoint, status, message, response);
    }

}
