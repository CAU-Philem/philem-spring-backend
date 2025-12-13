package com.philem.philem.domain.listing.entity;

import com.philem.philem.domain.model.entity.ItemModel;
import com.philem.philem.domain.pricing.entity.SaleRecord;
import com.philem.philem.domain.shared.enums.ConditionType;
import com.philem.philem.domain.shared.enums.PriceType;
import com.philem.philem.domain.shared.enums.UnitType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Table(name = "ListingItem")
public class ListingItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "listing_id", nullable = false)
    private Listing listing;

    private String postUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id", nullable = false)
    private ItemModel itemModel;

    @Enumerated(EnumType.STRING)
    private UnitType unitType;

    /*
    @Column(nullable = false)
    private Long quantity;
     */
    
    @Enumerated(EnumType.STRING)
    private PriceType priceType;

    @Column(nullable = true)
    private Long price;

    @Column(nullable = true)
    private Integer bundleIndex;

    private Boolean warranty;

    @Enumerated(EnumType.STRING)
    private ConditionType condition;        //A, B, C


    /*
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bundle_id", nullable = false)
    private Bundle bundle;
    */
    @OneToOne(mappedBy = "listingItem", fetch = FetchType.LAZY)
    private SaleRecord saleRecord;

    private LocalDateTime createdAt; //literally when this table entry was created, don't know why it's there but it is
}
