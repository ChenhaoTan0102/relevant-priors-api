package com.chenhao.relevantpriors.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PredictionDto {
    private String caseId;
    private String studyId;
    private boolean predictedIsRelevant;
}
