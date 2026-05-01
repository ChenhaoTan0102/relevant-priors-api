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
public class CaseDto {
    private String caseId;
    private String patientId;
    private String patientName;
    private StudyDto currentStudy;
    private List<StudyDto> priorStudies = new ArrayList<>();
}
