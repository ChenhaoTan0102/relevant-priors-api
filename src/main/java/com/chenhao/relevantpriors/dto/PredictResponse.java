package com.chenhao.relevantpriors.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PredictResponse {
    private List<PredictionDto> predictions = new ArrayList<>();
}
