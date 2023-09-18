package com.ceiti.nbugaenco.anagram.service;

import com.ceiti.nbugaenco.anagram.model.AnagramRequest;
import com.ceiti.nbugaenco.anagram.model.AnagramResponse;
import com.ceiti.nbugaenco.anagram.service.helper.AnagramServiceHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AnagramServiceTest {

    private static final String SOURCE = "TEST_SOURCE";
    private static final String TARGET = "TEST_TARGET";

    @Mock
    private AnagramResponse anagramResponse;
    @Mock
    private AnagramRequest anagramRequest;
    @Mock
    private DictionaryService dictionaryService;
    @Mock
    private AnagramServiceHelper anagramServiceHelper;
    @InjectMocks
    private AnagramService anagramService;

    @Test
    public void shouldCheckSimpleAnagram() {
        when(anagramRequest.getCheckRealWord()).thenReturn(Boolean.FALSE);
        when(anagramRequest.getTarget()).thenReturn(TARGET);
        when(anagramRequest.getSource()).thenReturn(SOURCE);
        when(anagramServiceHelper.checkInputs(TARGET, SOURCE)).thenReturn(Boolean.TRUE);
        when(anagramServiceHelper.asSimpleResponse(Boolean.TRUE)).thenReturn(anagramResponse);

        StepVerifier
                .create(anagramService.checkAnagram(anagramRequest))
                .expectNext(anagramResponse)
                .verifyComplete();

        verify(anagramRequest).getCheckRealWord();
        verify(anagramRequest).getTarget();
        verify(anagramRequest).getSource();
        verify(anagramServiceHelper).checkInputs(TARGET, SOURCE);
        verify(anagramServiceHelper).asSimpleResponse(Boolean.TRUE);
    }

    @Test
    public void shouldCheckRealWordAnagram() {
        when(anagramRequest.getCheckRealWord()).thenReturn(Boolean.TRUE);
        when(anagramRequest.getTarget()).thenReturn(TARGET);
        when(anagramRequest.getSource()).thenReturn(SOURCE);
        when(anagramServiceHelper.checkInputs(TARGET, SOURCE)).thenReturn(Boolean.TRUE);
        when(anagramServiceHelper.asSimpleResponse(Boolean.TRUE)).thenReturn(anagramResponse);
        when(dictionaryService.isRealWordByDictionary(SOURCE)).thenReturn(Mono.just(Boolean.TRUE));
        when(anagramServiceHelper.asRealWordResponse(anagramResponse, Boolean.TRUE)).thenReturn(anagramResponse);

        StepVerifier
                .create(anagramService.checkAnagram(anagramRequest))
                .expectNext(anagramResponse)
                .verifyComplete();

        verify(anagramRequest).getCheckRealWord();
        verify(anagramRequest).getTarget();
        verify(anagramRequest, times(2)).getSource();
        verify(anagramServiceHelper).checkInputs(TARGET, SOURCE);
        verify(anagramServiceHelper).asSimpleResponse(Boolean.TRUE);
        verify(dictionaryService).isRealWordByDictionary(SOURCE);
        verify(anagramServiceHelper).asRealWordResponse(anagramResponse, Boolean.TRUE);
    }

}