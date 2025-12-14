package com.philem.philem.domain.recommendation.controller;

import com.philem.philem.domain.recommendation.dto.RecommendationsResponse;
import com.philem.philem.domain.recommendation.entity.ItemTypeOption;
import com.philem.philem.domain.recommendation.entity.RecommendationSort;
import com.philem.philem.domain.recommendation.service.RecommendationService;
import com.philem.philem.domain.shared.enums.ConditionType;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recommendations")
public class RecommendationController {
    private final RecommendationService recommendationService;

    @GetMapping
    public RecommendationsResponse recommendations(
            @RequestParam Long modelId,
            @RequestParam Long userRegionId,
            @RequestParam(required = false) Integer radiusKm,
            @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) ConditionType condition,
            @RequestParam(required = false) ItemTypeOption itemType
    ) {
        System.out.println("[REQ] condition = " + condition);
        return recommendationService.recommend(modelId, userRegionId, radiusKm, limit, condition, itemType);
    }
}
