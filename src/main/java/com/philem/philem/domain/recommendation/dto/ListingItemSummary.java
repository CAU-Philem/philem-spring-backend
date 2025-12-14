package com.philem.philem.domain.recommendation.dto;

import com.philem.philem.domain.listing.entity.ListingItem;
import com.philem.philem.domain.shared.enums.ConditionType;

import java.time.LocalDateTime;

public record ListingItemSummary(
        Long listing_seq,
        String listing_id,
        Long model_id,
        Long region_id,
        Long price,
        ConditionType condition,
        String post_url,
        String thumbnail_url,
        LocalDateTime updated_at
) {
    public static ListingItemSummary from(ListingItem li) {
        return new ListingItemSummary(
                li.getListing().getSeq(),
                li.getListing().getId(),
                li.getItemModel().getId(),
                li.getListing().getRegionId(),
                li.getPrice(),
                li.getCondition(),
                li.getListing().getPostUrl(),
                li.getListing().getThumbnailUrl(),
                li.getListing().getBoostedAt()
        );
    }
}