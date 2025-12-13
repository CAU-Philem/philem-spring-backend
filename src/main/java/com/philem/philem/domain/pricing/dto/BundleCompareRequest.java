package com.philem.philem.domain.pricing.dto;

import com.philem.philem.domain.shared.enums.ConditionType;

import java.util.List;

public record BundleCompareRequest(
        Long bundle_price,
        List<BundleCompareItem> items
) {
    public record BundleCompareItem(
            Long model_id,
            ConditionType condition
    ) {}
}
