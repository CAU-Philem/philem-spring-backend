package com.philem.philem.domain.region.repository;

import com.philem.philem.domain.region.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RegionRepository extends JpaRepository<Region, Long> {

    @Query("select r from Region r where r.id = :id")
    Optional<Region> findById(@Param("id") Long id);
}
