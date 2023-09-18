package com.ceiti.nbugaenco.anagram.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.With;


@Data
@With
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public final class AnagramResponse {

    private final Boolean result;
    private final Boolean realWord;
    private final String reason;

}
