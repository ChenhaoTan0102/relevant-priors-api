package com.chenhao.relevantpriors.service;

import com.chenhao.relevantpriors.dto.CaseDto;
import com.chenhao.relevantpriors.dto.PredictRequest;
import com.chenhao.relevantpriors.dto.PredictResponse;
import com.chenhao.relevantpriors.dto.PredictionDto;
import com.chenhao.relevantpriors.dto.StudyDto;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PredictionService {

    private static final Logger log = LoggerFactory.getLogger(PredictionService.class);
    public PredictResponse predict(PredictRequest request, String requestId) {
        List<PredictionDto> predictions = new ArrayList<>();
        List<CaseDto> cases = request.getCases() != null ? request.getCases() : List.of();

        int totalPriors = 0;
        for (CaseDto c : cases) {
            List<StudyDto> priors =
                    c.getPriorStudies() != null ? c.getPriorStudies() : List.of();
            totalPriors += priors.size();
        }

        log.info(
                "predict requestId={} challengeId={} caseCount={} priorCount={}",
                requestId,
                request.getChallengeId(),
                cases.size(),
                totalPriors);

        for (CaseDto c : cases) {
            String caseId = c.getCaseId();
            StudyDto current = c.getCurrentStudy();
            String currentDesc = current != null ? current.getStudyDescription() : null;
            List<StudyDto> priors =
                    c.getPriorStudies() != null ? c.getPriorStudies() : List.of();

            for (StudyDto prior : priors) {
                String studyId = prior.getStudyId();
                String priorDesc = prior.getStudyDescription();
                boolean relevant = isRelevant(currentDesc, priorDesc);
                predictions.add(new PredictionDto(caseId, studyId, relevant));
            }
        }

        log.debug("predict requestId={} predictionCount={}", requestId, predictions.size());
        return new PredictResponse(predictions);
    }
    boolean isRelevant(String currentDescription, String priorDescription) {
        String cur = safeUpper(currentDescription);
        String pri = safeUpper(priorDescription);
        if (cur.isEmpty() || pri.isEmpty()) {
            return false;
        }

        if (headOrBrain(cur) && headOrBrain(pri)) {
            return true;
        }

        boolean sameMri = cur.contains("MRI") && pri.contains("MRI");
        boolean sameCt = cur.contains("CT") && pri.contains("CT");
        if ((sameMri || sameCt) && anatomicalOverlap(cur, pri)) {
            return true;
        }

        return false;
    }

    private static String safeUpper(String s) {
        return s == null ? "" : s.toUpperCase();
    }

    private static boolean headOrBrain(String d) {
        return d.contains("BRAIN") || d.contains("HEAD") || d.contains("SKULL") || d.contains("CEREB");
    }

    
    private static boolean anatomicalOverlap(String cur, String pri) {
        String[][] groups = {
            {"BRAIN", "HEAD", "SKULL", "CEREB", "ICA", "INTRACRAN"},
            {"CHEST", "THORAX", "LUNG", "CARDIAC", "HEART", "MEDIAST"},
            {"ABDOM", "LIVER", "GALL", "PANCREAS", "KIDNEY", "RENAL", "SPLEEN", "BOWEL"},
            {"SPINE", "C-SPINE", "T-SPINE", "L-SPINE", "CERVICAL", "THORACIC", "LUMBAR", "SACR"},
            {"NECK", "CAROTID", "SOFT TISSUE NECK"},
            {"BREAST", "MAMMO"},
            {"PELVIS", "HIP", "PROSTATE", "BLADDER", "UTERUS", "OVAR"},
            {"KNEE", "SHOULDER", "ANKLE", "WRIST", "ELBOW", "FOOT", "HAND", "MSK", "EXTREM"},
        };
        for (String[] g : groups) {
            if (containsAny(cur, g) && containsAny(pri, g)) {
                return true;
            }
        }
        return false;
    }

    private static boolean containsAny(String text, String[] tokens) {
        for (String t : tokens) {
            if (text.contains(t)) {
                return true;
            }
        }
        return false;
    }
}
