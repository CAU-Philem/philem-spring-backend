package com.philem.philem.domain.search.repository;

import com.philem.philem.domain.search.dto.ListingSearchCondition;
import com.philem.philem.domain.search.dto.ListingSearchResponse;

public interface ListingSearchRepository {
    ListingSearchResponse search(ListingSearchCondition condition);
}