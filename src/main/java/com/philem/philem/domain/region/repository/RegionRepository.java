package com.philem.philem.domain.region.repository;

import com.philem.philem.domain.region.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RegionRepository extends JpaRepository<Region, Long> {

    @Query("select r from Region r where r.id = :id")
    Optional<Region> findById(@Param("id") Long id);

    @Query(value = """
        SELECT r.*
        FROM regions r
        WHERE r.name LIKE CONCAT('%', :q, '%')
        ORDER BY
          CASE
            WHEN r.name = :q THEN 0
            WHEN r.name LIKE CONCAT(:q, '%') THEN 1
            ELSE 2
          END,
          CHAR_LENGTH(r.name) ASC,
          r.id ASC
        LIMIT :limit
        """, nativeQuery = true)
    List<Region> searchByNameRanked(
            @Param("q") String q,
            @Param("limit") int limit
    );
}
