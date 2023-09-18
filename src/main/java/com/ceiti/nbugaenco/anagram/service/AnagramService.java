package com.ceiti.nbugaenco.anagram.service;

import com.ceiti.nbugaenco.anagram.model.AnagramRequest;
import com.ceiti.nbugaenco.anagram.model.AnagramResponse;
import com.ceiti.nbugaenco.anagram.service.helper.AnagramServiceHelper;
import com.ceiti.nbugaenco.anagram.util.LogMessageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnagramService {

    private static final String ANAGRAM_CHECK_START = "ANAGRAM_CHECK_START";
    private static final String ANAGRAM_CHECK_FAILED = "ANAGRAM_CHECK_FAILED";
    private static final String SIMPLE_ANAGRAM_PROCESS_START = "SIMPLE_ANAGRAM_PROCESS_START";
    private static final String REAL_WORD_ANAGRAM_PROCESS_START = "REAL_WORD_ANAGRAM_PROCESS_START";
    private static final String BUILD_SIMPLE_RESPONSE = "BUILD_SIMPLE_RESPONSE";
    private static final String BUILD_REAL_WORD_RESPONSE = "BUILD_REAL_WORD_RESPONSE";

    private static final String ANAGRAM_CHECK_START_MESSAGE = "Started checking anagram";
    private static final String ANAGRAM_CHECK_FAILED_MESSAGE = "Anagram check failed";
    private static final String SIMPLE_ANAGRAM_PROCESS_START_MESSAGE = "Started processing anagram (without checking dictionary)";
    private static final String REAL_WORD_ANAGRAM_PROCESS_START_MESSAGE = "Checking if anagram is a real word";
    private static final String BUILD_SIMPLE_RESPONSE_MESSAGE = "Building simple response from anagram request";
    private static final String BUILD_REAL_WORD_RESPONSE_MESSAGE = "Building real word response from simple response";

    private final DictionaryService dictionaryService;
    private final AnagramServiceHelper anagramServiceHelper;

    public Mono<AnagramResponse> checkAnagram(final AnagramRequest anagramRequest) {
        return Mono
                .just(anagramRequest)
                .doOnNext(anagramRequest1 -> LogMessageUtil.logInfo(log, ANAGRAM_CHECK_START,
                        ANAGRAM_CHECK_START_MESSAGE, anagramRequest1))
                .filter(AnagramRequest::getCheckRealWord)
                .flatMap(this::processRealWordAnagram)
                .switchIfEmpty(Mono.defer(() -> processSimpleAnagram(anagramRequest)))
                .doOnError(cause -> LogMessageUtil.logError(log, ANAGRAM_CHECK_FAILED,
                        ANAGRAM_CHECK_FAILED_MESSAGE, cause, anagramRequest));
    }

    private Mono<AnagramResponse> processRealWordAnagram(final AnagramRequest anagramRequest) {
        return Mono
                .just(anagramRequest)
                .flatMap(this::processSimpleAnagram)
                .doOnNext(anagramRequest1 -> LogMessageUtil.logInfo(log, REAL_WORD_ANAGRAM_PROCESS_START,
                        REAL_WORD_ANAGRAM_PROCESS_START_MESSAGE, anagramRequest1))
                .zipWith(dictionaryService.isRealWordByDictionary(anagramRequest.getSource()))
                .doOnNext(tuple -> LogMessageUtil.logInfo(log, BUILD_REAL_WORD_RESPONSE,
                        BUILD_REAL_WORD_RESPONSE_MESSAGE, tuple))
                .map(tuple -> anagramServiceHelper.asRealWordResponse(tuple.getT1(), tuple.getT2()));
    }

    private Mono<AnagramResponse> processSimpleAnagram(final AnagramRequest anagramRequest) {
        return Mono
                .just(anagramRequest)
                .doOnNext(anagramRequest1 -> LogMessageUtil.logInfo(log, SIMPLE_ANAGRAM_PROCESS_START,
                        SIMPLE_ANAGRAM_PROCESS_START_MESSAGE, anagramRequest1))
                .map(anagramRequest1 ->
                        anagramServiceHelper.checkInputs(anagramRequest.getTarget(), anagramRequest.getSource()))
                .doOnNext(anagramRequest1 -> LogMessageUtil.logInfo(log, BUILD_SIMPLE_RESPONSE,
                        BUILD_SIMPLE_RESPONSE_MESSAGE, anagramRequest1))
                .map(anagramServiceHelper::asSimpleResponse);
    }

}
