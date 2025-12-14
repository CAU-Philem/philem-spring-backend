package com.philem.philem.domain.region.dto;

import com.philem.philem.domain.region.entity.Region;

public record RegionSearchResponse(
        Long id,
        String name,     // "서울특별시 강남구 역삼동" 같은 풀네임 권장
        Double lat,
        Double lng
) {
    public static RegionSearchResponse from(Region r) {
        return new RegionSearchResponse(
                r.getId(),
                r.getName(),
                r.getLat(),
                r.getLng()
        );
    }
}
