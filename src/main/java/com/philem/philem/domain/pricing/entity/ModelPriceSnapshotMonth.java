package com.philem.philem.domain.pricing.entity;

import com.philem.philem.domain.model.entity.ItemModel;
import com.philem.philem.domain.shared.enums.ConditionType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Table(name = "model_price_snapshot_month")
public class ModelPriceSnapshotMonth {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id", nullable = false)
    private ItemModel itemModel;

    private Integer soldYear;

    private Integer soldMonth;

    @Enumerated(EnumType.STRING)
    private ConditionType condition;

    private Long maxPrice;

    private Long minPrice;

    private Long avgPrice;

    private Long sampleCount;
}
