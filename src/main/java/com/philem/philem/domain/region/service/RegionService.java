package com.philem.philem.domain.region.service;

import com.philem.philem.domain.region.dto.RegionSearchResponse;
import com.philem.philem.domain.region.entity.Region;
import com.philem.philem.domain.region.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RegionService {

    private final RegionRepository regionRepository;

    public List<RegionSearchResponse> search(String partialInput, Integer limit) {
        String keyword = (partialInput == null) ? "" : partialInput.trim();
        if (keyword.isBlank()) return List.of();

        int lim = (limit == null) ? 10 : Math.max(1, Math.min(limit, 30));

        List<Region> regions = regionRepository.searchByNameRanked(keyword, lim);

        // lat/lng 없는 row는 자동완성에서 제외 (추천 API에서 에러나니까)
        return regions.stream()
                .filter(r -> r.getLat() != null && r.getLng() != null)
                .map(RegionSearchResponse::from)
                .toList();
    }
}
