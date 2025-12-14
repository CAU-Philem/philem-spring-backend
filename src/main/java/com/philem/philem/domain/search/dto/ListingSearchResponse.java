package com.philem.philem.domain.search.dto;

import lombok.Builder;
import lombok.Value;
import java.util.List;

@Value
@Builder
public class ListingSearchResponse {
    List<ListingSearchItem> items;
    int page;
    int size;
    long total;
}