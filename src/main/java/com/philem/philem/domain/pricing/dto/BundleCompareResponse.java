package com.philem.philem.domain.pricing.dto;

import com.philem.philem.domain.shared.enums.ConditionType;

import java.util.List;

public record BundleCompareResponse(
        Long bundle_price,
        Boolean available,
        String reason,

        Integer ref_year,               // max(item.refYear)
        Integer ref_month,              // max(item.refMonth)
        Long ref_price,                 // sum(item.refPrice)

        Long diff_price,
        Double percent_vs_ref,      // percent
        String direction,

        List<ItemBreakdown> items
) {
    public record ItemBreakdown(
            Long model_id,
            ConditionType condition,
            Integer ref_year,
            Integer ref_month,
            Long ref_avg_price
    ) {}
}
