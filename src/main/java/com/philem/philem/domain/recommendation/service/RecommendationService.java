package com.philem.philem.domain.recommendation.service;

import com.philem.philem.domain.listing.entity.ListingItem;
import com.philem.philem.domain.listing.repository.ListingItemRepository;
import com.philem.philem.domain.recommendation.dto.ListingItemSummary;
import com.philem.philem.domain.recommendation.dto.RecommendationsResponse;
import com.philem.philem.domain.recommendation.entity.ItemTypeOption;
import com.philem.philem.domain.region.entity.Region;
import com.philem.philem.domain.region.repository.RegionRepository;
import com.philem.philem.domain.shared.enums.ConditionType;
import com.philem.philem.domain.shared.enums.PriceType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecommendationService {
    private final RegionRepository regionRepository;
    private final ListingItemRepository listingItemRepository;

    public RecommendationsResponse recommend(
            Long modelId, Long userRegionId, Integer radiusKm, Integer limit,
            ConditionType condition, ItemTypeOption itemType
    ) {
        int rKm = (radiusKm == null) ? 10 : radiusKm;
        int radiusM = rKm * 1000;
        int lim = (limit == null) ? 120 : Math.min(limit, 300);

        Region userRegion = regionRepository.findById(userRegionId)
                .orElseThrow(() -> new IllegalArgumentException("userRegionId not found: " + userRegionId));

        if (userRegion.getLat() == null || userRegion.getLng() == null) {
            throw new IllegalStateException("user region has no lat/lng: " + userRegionId);
        }

        // SINGLE/BUNDLE -> 실제 DB price_type 값으로 매핑
        String priceTypeFilter = null;
        if (itemType != null) {
            priceTypeFilter = (itemType == ItemTypeOption.SINGLE) ? "PER_ITEM" : "BUNDLE_SHARED";
        }

        String conditionFilter = (condition == null) ? null : condition.name();

        List<ListingItem> items = listingItemRepository.findNearbySameModelPrice(
                modelId,
                userRegion.getLat(),
                userRegion.getLng(),
                radiusM,
                lim,
                conditionFilter,
                priceTypeFilter
        );

        // 번들 여부도 count 말고 price_type으로 판별
        Map<ConditionType, List<ListingItemSummary>> byCondition =
                items.stream()
                        .map(li -> {
                            boolean isBundle = "BUNDLE_SHARED".equals(li.getPriceType().name()); // 또는 li.getPriceType() 비교
                            long price = isBundle
                                    ? li.getListing().getPrice()
                                    : (li.getPrice() != null && li.getPrice() > 0 ? li.getPrice() : li.getListing().getPrice());
                            return ListingItemSummary.from(li, isBundle, price);
                        })
                        .collect(Collectors.groupingBy(
                                ListingItemSummary::condition,
                                () -> new EnumMap<>(ConditionType.class),
                                Collectors.toList()
                        ));

        return new RecommendationsResponse(modelId, userRegionId, rKm, byCondition);
    }



}
