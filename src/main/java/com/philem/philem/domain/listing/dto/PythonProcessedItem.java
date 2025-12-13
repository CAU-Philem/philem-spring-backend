package com.philem.philem.domain.listing.dto;

import com.philem.philem.domain.shared.enums.ConditionType;
import com.philem.philem.domain.shared.enums.PriceType;

public record PythonProcessedItem(
        Long model_id,
        String model_name,
        ConditionType condition,
        Long price,
        PriceType price_type,
        String role,
        int bundle_index
) {
}
