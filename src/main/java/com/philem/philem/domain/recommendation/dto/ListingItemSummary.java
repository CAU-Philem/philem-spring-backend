package com.philem.philem.domain.recommendation.dto;

import com.philem.philem.domain.listing.entity.ListingItem;
import com.philem.philem.domain.shared.enums.ConditionType;

import java.time.LocalDateTime;

public record ListingItemSummary(
        Long listing_seq,
        String listing_id,
        Long model_id,
        Long region_id,
        String region_name,

        Long price,           // "정렬/표시"에 쓰는 가격 (단일=li.price, 번들=l.price)
        Boolean is_bundle,    // 번들 여부

        ConditionType condition,
        String post_url,
        String thumbnail_url,
        LocalDateTime boosted_at
) {
    public static ListingItemSummary from(ListingItem li, Boolean isBundle, Long price) {
        return new ListingItemSummary(
                li.getListing().getSeq(),
                li.getListing().getId(),
                li.getItemModel().getId(),
                li.getListing().getRegionId(),
                li.getListing().getRegion().getName(),
                price,
                isBundle,
                li.getCondition(),
                li.getListing().getPostUrl(),
                li.getListing().getThumbnailUrl(),
                li.getListing().getBoostedAt()
        );
    }
}