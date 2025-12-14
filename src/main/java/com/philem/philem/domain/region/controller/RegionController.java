package com.philem.philem.domain.region.controller;

import com.philem.philem.domain.region.dto.RegionSearchResponse;
import com.philem.philem.domain.region.service.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/regions")
public class RegionController {

    private final RegionService regionService;

    /**
     * 자동완성/검색:
     * GET /api/regions/search?q=역삼&limit=10
     */
    @GetMapping("/search")
    public List<RegionSearchResponse> search(
            @RequestParam String partialInput,
            @RequestParam(required = false) Integer limit
    ) {
        return regionService.search(partialInput, limit);
    }
}
