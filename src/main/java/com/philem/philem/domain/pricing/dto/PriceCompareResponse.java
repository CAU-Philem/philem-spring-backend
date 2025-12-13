package com.philem.philem.domain.pricing.dto;

import com.philem.philem.domain.shared.enums.ConditionType;

public record PriceCompareResponse(
        Long model_id,
        ConditionType condition,
        Long input_price,
        Boolean available,                      // e.g., never sold before
        String reason,                          // why unavailable or extra info
        Integer ref_year,
        Integer ref_month,
        Long ref_avg_price,
        Long diff_price,
        Double percent_vs_ref,
        String direction
) {
}
