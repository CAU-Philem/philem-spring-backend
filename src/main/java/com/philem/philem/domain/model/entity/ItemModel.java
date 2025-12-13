package com.philem.philem.domain.model.entity;

import com.philem.philem.domain.listing.entity.ListingItem;
import com.philem.philem.domain.shared.enums.UnitType;
import com.philem.philem.domain.pricing.entity.SaleRecord;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Table(name = "itemModel")
public class ItemModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String brand;

    private UnitType unitType;

    @Enumerated(EnumType.STRING)
    private CameraType cameraType;

    private String mount;

    @OneToMany(mappedBy = "itemModel")
    private List<ListingItem> listingItems = new ArrayList<>();

    @OneToMany(mappedBy = "itemModel")
    private List<SaleRecord> SaleRecords = new ArrayList<>();
}
