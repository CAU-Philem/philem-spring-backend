package com.philem.philem.domain.recommendation.dto;

import com.philem.philem.domain.shared.enums.ConditionType;

import java.util.List;
import java.util.Map;

public record RecommendationsResponse(
        Long model_id,
        Long user_region_id,
        Integer radius_km,
        Map<ConditionType, List<ListingItemSummary>> by_condition
) {

}