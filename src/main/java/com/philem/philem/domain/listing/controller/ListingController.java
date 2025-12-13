package com.philem.philem.domain.listing.controller;

import com.philem.philem.domain.listing.dto.KarrotAnalyzeRequest;
import com.philem.philem.domain.listing.dto.PythonUrlResponse;
import com.philem.philem.domain.listing.service.ListingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/listings")
public class ListingController {

    private final ListingService listingService;
    // url 주면 개시물 분석해서 개시물 id, 개시물 url, 개시물에 있는 모든 모델명, 모델 id, 각각 condition,등을 반환해줌
    @PostMapping("/models-from-url")
    public PythonUrlResponse analyze(@RequestBody KarrotAnalyzeRequest request) {
        return listingService.getModelsForUrl(request.url());
    }
}
