package com.chenhao.relevantpriors.controller;

import com.chenhao.relevantpriors.dto.PredictRequest;
import com.chenhao.relevantpriors.dto.PredictResponse;
import com.chenhao.relevantpriors.service.PredictionService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.UUID;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/predict")
public class PredictController {
    private final PredictionService predictionService;
    public PredictController(PredictionService predictionService) {
        this.predictionService = predictionService;
    }
    @PostMapping
    public PredictResponse predict(@RequestBody PredictRequest request, HttpServletRequest http) {
        String requestId =
                Optional.ofNullable(http.getHeader("X-Request-Id"))
                        .or(() -> Optional.ofNullable(http.getHeader("X-Request-ID")))
                        .orElseGet(() -> UUID.randomUUID().toString());
        return predictionService.predict(request, requestId);
    }
}
