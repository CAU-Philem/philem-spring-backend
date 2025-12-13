package com.philem.philem.domain.pricing.util;

import com.philem.philem.domain.pricing.dto.ModelPriceSnapshotMonthResponse;
import com.philem.philem.domain.pricing.entity.ModelPriceSnapshotMonth;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ModelPriceSnapshotMonthMapper {

    public static ModelPriceSnapshotMonthResponse toModelPriceSnapshotMonthResponse(ModelPriceSnapshotMonth snapshot) {
        return ModelPriceSnapshotMonthResponse.builder()
                .condition(snapshot.getCondition())
                .sold_year(snapshot.getSoldYear())
                .sold_month(snapshot.getSoldMonth())
                .max_price(snapshot.getMaxPrice())
                .min_price(snapshot.getMinPrice())
                .avg_price(snapshot.getAvgPrice())
                .sample_count(snapshot.getSampleCount())
                .build();
    }
}
