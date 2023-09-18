package com.ceiti.nbugaenco.anagram.controller;

import com.ceiti.nbugaenco.anagram.model.AnagramRequest;
import com.ceiti.nbugaenco.anagram.model.AnagramResponse;
import com.ceiti.nbugaenco.anagram.service.AnagramService;
import com.ceiti.nbugaenco.anagram.util.LogMessageUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(AnagramController.ANAGRAM_ENDPOINT)
public class AnagramController {

    public static final Duration DEFAULT_TIMEOUT = Duration.ofMillis(5000);

    public static final String ANAGRAM_ENDPOINT = "/anagram";
    public static final String CHECK_ANAGRAM_REQUEST_START_MESSAGE = "Started checking anagram";
    public static final String CHECK_ANAGRAM_RESPONSE_SUCCESS_MESSAGE = "Anagram checked with success";
    private static final String CHECK_ANAGRAM_REQUEST_START = "CHECK_ANAGRAM_REQUEST_START";
    private static final String CHECK_ANAGRAM_RESPONSE_SUCCESS = "CHECK_ANAGRAM_RESPONSE_SUCCESS";
    private static final String CHECK_ANAGRAM_REQUEST_FAILED = "CHECK_ANAGRAM_REQUEST_FAILED";
    private static final String CHECK_ANAGRAM_REQUEST_FAILED_MESSAGE = "Something went wrong during checking anagram";

    private final AnagramService anagramService;

    @PostMapping
    public Mono<AnagramResponse> check(@Valid @RequestBody final Mono<AnagramRequest> anagramRequest) {
        return anagramRequest
                .doOnNext(anagramRequest1 -> LogMessageUtil.logRequest(log, CHECK_ANAGRAM_REQUEST_START,
                        HttpMethod.POST, ANAGRAM_ENDPOINT, CHECK_ANAGRAM_REQUEST_START_MESSAGE, anagramRequest1))
                .flatMap(anagramService::checkAnagram)
                .doOnNext(anagramResponse -> LogMessageUtil.logResponse(log, CHECK_ANAGRAM_RESPONSE_SUCCESS,
                        HttpMethod.POST, ANAGRAM_ENDPOINT, HttpStatus.OK,
                        CHECK_ANAGRAM_RESPONSE_SUCCESS_MESSAGE, anagramResponse))
                .doOnError(cause -> LogMessageUtil.logError(log, CHECK_ANAGRAM_REQUEST_FAILED,
                        CHECK_ANAGRAM_REQUEST_FAILED_MESSAGE, cause, anagramRequest))
                .timeout(DEFAULT_TIMEOUT);
    }
}
