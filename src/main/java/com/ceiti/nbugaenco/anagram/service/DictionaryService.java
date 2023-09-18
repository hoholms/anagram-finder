package com.ceiti.nbugaenco.anagram.service;

import com.ceiti.nbugaenco.anagram.configuration.WebClientConfiguration;
import com.ceiti.nbugaenco.anagram.exception.DictionaryException;
import com.ceiti.nbugaenco.anagram.service.helper.AnagramServiceHelper;
import com.ceiti.nbugaenco.anagram.util.LogMessageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class DictionaryService {

    public static final String DICTIONARY_RESPONSE_FOUND = "DICTIONARY_REQUEST_FOUND";
    public static final String DICTIONARY_RESPONSE_FOUND_MESSAGE = "Found word in dictionary";
    private static final String DICTIONARY_REQUEST_STARTED = "DICTIONARY_REQUEST_STARTED";
    private static final String DICTIONARY_RESPONSE_NOT_FOUND = "DICTIONARY_REQUEST_NOT_FOUND";
    private static final String DICTIONARY_REQUEST_FAILED = "DICTIONARY_REQUEST_FAILED";
    private static final String DICTIONARY_REQUEST_STARTED_MESSAGE = "Sending request to Dictionary API";
    private static final String DICTIONARY_RESPONSE_NOT_FOUND_MESSAGE = "Not found word in dictionary";
    private static final String DICTIONARY_REQUEST_FAILED_MESSAGE =
            "Something went wrong during the request to Dictionary API";

    private final AnagramServiceHelper anagramServiceHelper;
    private final WebClient webClient;

    public Mono<Boolean> isRealWordByDictionary(final String word) {
        LogMessageUtil.logRequest(log, DICTIONARY_REQUEST_STARTED, HttpMethod.GET, WebClientConfiguration.BASE_URL,
                DICTIONARY_REQUEST_STARTED_MESSAGE, word);
        return webClient
                .get()
                .uri(String.join("", anagramServiceHelper.clean(word)))
                .exchangeToMono(response -> {
                    if (response.statusCode().isSameCodeAs(HttpStatus.OK)) {
                        LogMessageUtil.logResponse(log, DICTIONARY_RESPONSE_FOUND, HttpMethod.GET,
                                WebClientConfiguration.BASE_URL, HttpStatus.resolve(response.statusCode().value()),
                                DICTIONARY_RESPONSE_FOUND_MESSAGE, word);
                        return Mono.just(Boolean.TRUE);
                    } else if (response.statusCode().isSameCodeAs(HttpStatus.NOT_FOUND)) {
                        LogMessageUtil.logResponse(log, DICTIONARY_RESPONSE_NOT_FOUND, HttpMethod.GET,
                                WebClientConfiguration.BASE_URL, HttpStatus.resolve(response.statusCode().value()),
                                DICTIONARY_RESPONSE_NOT_FOUND_MESSAGE, word);
                        return Mono.just(Boolean.FALSE);
                    } else {
                        return Mono.error(new DictionaryException(DICTIONARY_REQUEST_FAILED,
                                DICTIONARY_REQUEST_FAILED_MESSAGE, HttpStatus.BAD_REQUEST, null));
                    }
                })
                .doOnError(cause -> LogMessageUtil.logError(log, DICTIONARY_REQUEST_FAILED,
                        DICTIONARY_REQUEST_FAILED_MESSAGE, cause, word));
    }


}
