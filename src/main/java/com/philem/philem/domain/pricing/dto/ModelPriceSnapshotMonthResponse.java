package com.philem.philem.domain.pricing.dto;

import com.philem.philem.domain.shared.enums.ConditionType;
import lombok.Builder;

@Builder
public record ModelPriceSnapshotMonthResponse(
        ConditionType condition,
        int sold_year,
        int sold_month,
        Long max_price,
        Long min_price,
        Long avg_price,
        Long sample_count
) {

}
