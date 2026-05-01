package com.chenhao.relevantpriors.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PredictRequest {
    private String challengeId;
    private Integer schemaVersion;
    private String generatedAt;
    private List<CaseDto> cases = new ArrayList<>();
}
