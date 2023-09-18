package com.ceiti.nbugaenco.anagram.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.With;

import java.io.Serializable;

@Data
@With
@Builder
public final class AnagramRequest implements Serializable {

    @NotBlank(message = "Provide target")
    private final String target;
    @NotBlank(message = "Provide source")
    private final String source;
    private final Boolean checkRealWord = Boolean.FALSE;

}
