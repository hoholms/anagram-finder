package com.ceiti.nbugaenco.anagram.service.helper;

import com.ceiti.nbugaenco.anagram.model.AnagramResponse;
import io.netty.util.internal.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.IntStream;

@Component
public class AnagramServiceHelper {

    public static final String REGEX = "[\\p{Punct}\\s]+";
    public static final String ANAGRAM_REASON = "It is an anagram";
    public static final String NOT_ANAGRAM_REASON = "It is not an anagram";
    public static final String REAL_ANAGRAM_REASON = "It is an anagram and a real word";
    public static final String NOT_REAL_ANAGRAM_REASON = "It is an anagram but not a real word";
    public static final String REAL_NOT_ANAGRAM_REASON = "It is not an anagram but a real word";
    public static final String NOT_REAL_NOT_ANAGRAM_REASON = "It is not an anagram and not a real word";

    public String cleanAndSort(final String prompt) {
        return Optional.ofNullable(prompt)
                .map(prompt1 -> prompt1.replaceAll(REGEX, StringUtil.EMPTY_STRING))
                .map(String::toLowerCase)
                .map(String::chars)
                .map(IntStream::sorted)
                .map(intStream ->
                        intStream.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append))
                .map(StringBuilder::toString)
                .orElse(StringUtils.EMPTY);
    }

    public String clean(final String prompt) {
        return Optional.ofNullable(prompt)
                .map(prompt1 -> prompt1.replaceAll(REGEX, StringUtil.EMPTY_STRING))
                .orElse(null);
    }

    public AnagramResponse asSimpleResponse(Boolean result) {
        return AnagramResponse
                .builder()
                .result(result)
                .reason(asSimpleReason(result))
                .build();
    }

    public AnagramResponse asRealWordResponse(AnagramResponse anagramResponse, Boolean isRealWord) {
        return anagramResponse
                .withRealWord(isRealWord)
                .withReason(asRealWordReason(anagramResponse.getResult(), isRealWord));
    }

    public Boolean checkInputs(String target, String source) {
        return Optional
                .ofNullable(target)
                .map(this::cleanAndSort)
                .filter(cleanTarget -> StringUtils.equals(cleanTarget, cleanAndSort(source)))
                .isPresent();
    }

    private String asSimpleReason(Boolean result) {
        return Optional.ofNullable(result)
                .filter(Boolean::booleanValue)
                .map(reason -> ANAGRAM_REASON)
                .orElse(NOT_ANAGRAM_REASON);
    }

    private String asRealWordReason(Boolean result, Boolean isRealWord) {
        return Optional.of(result)
                .filter(Boolean::booleanValue)
                .map(result1 -> Boolean.TRUE.equals(isRealWord) ? REAL_ANAGRAM_REASON : NOT_REAL_ANAGRAM_REASON)
                .orElse(Boolean.TRUE.equals(isRealWord) ? REAL_NOT_ANAGRAM_REASON : NOT_REAL_NOT_ANAGRAM_REASON);
    }


}
