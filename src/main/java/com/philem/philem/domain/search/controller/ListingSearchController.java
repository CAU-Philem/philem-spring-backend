package com.philem.philem.domain.search.controller;

import com.philem.philem.domain.search.dto.ListingSearchRequest;
import com.philem.philem.domain.search.dto.ListingSearchResponse;
import com.philem.philem.domain.search.service.ListingSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/listings")
public class ListingSearchController {
    private final ListingSearchService listingSearchService;

    @GetMapping("/search")
    public ListingSearchResponse search(@ModelAttribute ListingSearchRequest req) {
        return listingSearchService.search(req);
    }
}