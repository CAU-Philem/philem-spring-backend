package com.philem.philem.domain.pricing.entity;


import com.philem.philem.domain.listing.entity.ListingItem;
import com.philem.philem.domain.model.entity.ItemModel;
import com.philem.philem.domain.region.entity.Region;
import com.philem.philem.domain.shared.enums.ConditionType;
import com.philem.philem.domain.shared.enums.MarketType;
import com.philem.philem.domain.shared.enums.PriceType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Table(name = "SaleRecord")
public class SaleRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "listing_item_id", nullable = false, unique = true)
    private ListingItem listingItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id", nullable = false)
    private ItemModel itemModel;

    private Long price;

    @Enumerated(EnumType.STRING)
    private ConditionType condition;

    private LocalDateTime soldAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id", nullable = false)
    private Region region;

    @Enumerated(EnumType.STRING)
    private MarketType marketType;

    @Enumerated(EnumType.STRING)
    private PriceType createdFrom;
}
