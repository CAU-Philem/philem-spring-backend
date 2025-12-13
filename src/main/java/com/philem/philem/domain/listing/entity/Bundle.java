package com.philem.philem.domain.listing.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Table(name = "Bundles")
public class Bundle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "listing_id", nullable = false)
    private Listing listing;

    private Integer bundleIndex;

    private Long totalPrice;

    /*
    @OneToMany(mappedBy = "bundle", fetch = FetchType.LAZY)
    private List<ListingItem> items = new ArrayList<>();

     */
}
