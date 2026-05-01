package com.chenhao.relevantpriors.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.chenhao.relevantpriors.dto.CaseDto;
import com.chenhao.relevantpriors.dto.PredictRequest;
import com.chenhao.relevantpriors.dto.PredictResponse;
import com.chenhao.relevantpriors.dto.StudyDto;
import java.util.List;
import org.junit.jupiter.api.Test;

class PredictionServiceTest {

    private final PredictionService predictionService = new PredictionService();

    @Test
    void onePredictionPerPrior_sameCaseIdAndStudyId() {
        StudyDto current =
                new StudyDto(
                        "3100042",
                        "MRI BRAIN STROKE LIMITED WITHOUT CONTRAST",
                        "2026-03-08");
        StudyDto prior1 =
                new StudyDto(
                        "2453245",
                        "MRI BRAIN STROKE LIMITED WITHOUT CONTRAST",
                        "2020-03-08");
        StudyDto prior2 = new StudyDto("992654", "CT HEAD WITHOUT CNTRST", "2021-03-08");

        CaseDto c =
                new CaseDto(
                        "1001016",
                        "606707",
                        "Andrews, Micheal",
                        current,
                        List.of(prior1, prior2));

        PredictRequest req = new PredictRequest();
        req.setChallengeId("relevant-priors-v1");
        req.setCases(List.of(c));

        PredictResponse out = predictionService.predict(req, "test-rid");

        assertThat(out.getPredictions()).hasSize(2);
        assertThat(out.getPredictions().get(0).getCaseId()).isEqualTo("1001016");
        assertThat(out.getPredictions().get(0).getStudyId()).isEqualTo("2453245");
        assertThat(out.getPredictions().get(0).isPredictedIsRelevant()).isTrue();
        assertThat(out.getPredictions().get(1).getStudyId()).isEqualTo("992654");
        assertThat(out.getPredictions().get(1).isPredictedIsRelevant()).isTrue();
    }

    @Test
    void emptyCases_returnsEmptyPredictions() {
        PredictRequest req = new PredictRequest();
        req.setCases(List.of());
        PredictResponse out = predictionService.predict(req, "empty");
        assertThat(out.getPredictions()).isEmpty();
    }
}
