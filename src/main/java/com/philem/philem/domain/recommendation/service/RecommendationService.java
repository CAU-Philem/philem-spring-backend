package com.philem.philem.domain.recommendation.service;

import com.philem.philem.domain.listing.entity.ListingItem;
import com.philem.philem.domain.listing.repository.ListingItemRepository;
import com.philem.philem.domain.recommendation.dto.ListingItemSummary;
import com.philem.philem.domain.recommendation.dto.RecommendationsResponse;
import com.philem.philem.domain.region.entity.Region;
import com.philem.philem.domain.region.repository.RegionRepository;
import com.philem.philem.domain.shared.enums.ConditionType;
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

    public RecommendationsResponse recommend(Long modelId, Long userRegionId, Integer radiusKm, Integer limit) {
        int rKm = (radiusKm == null) ? 10 : radiusKm;
        int radiusM = rKm * 1000;
        int lim = (limit == null) ? 120 : Math.min(limit, 300); // 과도한 응답 방지

        Region userRegion = regionRepository.findById(userRegionId)
                .orElseThrow(() -> new IllegalArgumentException("userRegionId not found: " + userRegionId));

        if (userRegion.getLat() == null || userRegion.getLng() == null) {
            throw new IllegalStateException("user region has no lat/lng: " + userRegionId);
        }

        List<ListingItem> items = listingItemRepository.findNearbySameModel(
                modelId,
                userRegion.getLat(),
                userRegion.getLng(),
                radiusM,
                lim
        );

        // condition별 그룹핑
        Map<ConditionType, List<ListingItemSummary>> byCondition =
                items.stream()
                        .map(ListingItemSummary::from)
                        .collect(Collectors.groupingBy(
                                ListingItemSummary::condition,
                                () -> new EnumMap<>(ConditionType.class),
                                Collectors.toList()
                        ));

        return new RecommendationsResponse(modelId, userRegionId, rKm, byCondition);
    }

    private String normalizeCondition(String c) {
        if (c == null) return "UNKNOWN";
        String u = c.trim().toUpperCase();
        return switch (u) {
            case "A", "B", "C" -> u;
            default -> "UNKNOWN";
        };
    }
}
