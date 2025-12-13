package com.philem.philem.domain.listing.entity;

import com.philem.philem.domain.shared.enums.MarketType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "listing")
public class Listing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private String id;

    private String title;

    private Long price;

    private String thumbnailUrl;

    private String postUrl;

    @Enumerated(EnumType.STRING)
    private StatusType status;

    private String description;

    private LocalDateTime createdAt;

    private LocalDateTime boostedAt;

    private Long regionId;

    @Enumerated(EnumType.STRING)
    private MarketType marketType;

    @OneToMany(mappedBy = "listing", fetch = FetchType.LAZY)
    private List<Bundle> bundles = new ArrayList<>();

    @OneToMany(mappedBy = "listing", fetch = FetchType.LAZY)
    private List<ListingItem> items = new ArrayList<>();
}
