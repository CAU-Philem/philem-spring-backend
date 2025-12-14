package com.philem.philem.domain.region.entity;

import com.philem.philem.domain.listing.entity.Listing;
import com.philem.philem.domain.pricing.entity.SaleRecord;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Table(name = "regions")
public class Region {
    @Id
    private Long id;

    private String name;

    private Double lat;

    private Double lng;

    @OneToMany(mappedBy = "region", fetch = FetchType.LAZY)
    private List<SaleRecord> saleRecords = new ArrayList<>();

    @OneToMany(mappedBy = "region", fetch = FetchType.LAZY)
    private List<Listing> listings = new ArrayList<>();
}
