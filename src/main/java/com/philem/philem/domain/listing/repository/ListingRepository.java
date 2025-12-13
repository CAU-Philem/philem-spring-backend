package com.philem.philem.domain.listing.repository;

import com.philem.philem.domain.listing.entity.Listing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListingRepository extends JpaRepository<Listing, Long> {
}
