package com.philem.philem.domain.listing.repository;

import com.philem.philem.domain.listing.entity.ListingItem;
import com.philem.philem.domain.shared.enums.ConditionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ListingItemRepository extends JpaRepository<ListingItem, Long> {

    @Query(value = """
        SELECT li.*
        FROM ListingItem li
        JOIN listing l ON l.seq = li.listing_id
        JOIN regions r ON r.id = l.region_id
        WHERE li.model_id = :modelId
          AND li.condition IS NOT NULL
          AND ST_Distance_Sphere(
                POINT(r.lng, r.lat),
                POINT(:userLng, :userLat)
              ) <= :radiusM
        ORDER BY l.boosted_at DESC
        LIMIT :limit
        """, nativeQuery = true)
    List<ListingItem> findNearbySameModel(
            @Param("modelId") Long modelId,
            @Param("userLat") double userLat,
            @Param("userLng") double userLng,
            @Param("radiusM") int radiusM,
            @Param("limit") int limit
    );

    @Query(value = """
        SELECT li.*
        FROM ListingItem li
        JOIN listing l ON l.seq = li.listing_id
        JOIN regions r ON r.id = CAST(l.region_id AS UNSIGNED)
    
        WHERE li.model_id = :modelId
          AND li.condition IS NOT NULL
          AND l.status = 'Active'
          AND (:condition IS NULL OR li.condition = :condition)
    
          -- 토글: SINGLE/BUNDLE (price_type으로)
          AND (:priceType IS NULL OR li.price_type = :priceType)
    
          AND ST_Distance_Sphere(
                POINT(r.lng, r.lat),
                POINT(:userLng, :userLat)
              ) <= :radiusM
    
        ORDER BY
          CASE
            WHEN (:priceType = 'BUNDLE_SHARED') THEN l.price
            ELSE COALESCE(NULLIF(li.price, 0), l.price)
          END ASC,
          l.boosted_at DESC,
          l.seq DESC
        LIMIT :limit
        """, nativeQuery = true)
    List<ListingItem> findNearbySameModelPrice(
            @Param("modelId") Long modelId,
            @Param("userLat") double userLat,
            @Param("userLng") double userLng,
            @Param("radiusM") int radiusM,
            @Param("limit") int limit,
            @Param("condition") String condition,
            @Param("priceType") String priceType
    );


}

