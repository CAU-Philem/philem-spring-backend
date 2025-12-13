package com.philem.philem.domain.listing.service;

import com.philem.philem.domain.listing.dto.PythonUrlRequest;
import com.philem.philem.domain.listing.dto.PythonUrlResponse;
import com.philem.philem.domain.listing.repository.ListingRepository;
import com.philem.philem.domain.pricing.service.ModelPriceSnapshotMonthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListingService {
    private final ListingRepository listingRepository;
    private final ModelPriceSnapshotMonthService modelPriceSnapshotMonthService;
    private final WebClient pythonWebClient;

    @Transactional
    public PythonUrlResponse getModelsForUrl(String url){
        return pythonWebClient.post()
                .uri("/listings/from-url")
                .bodyValue(new PythonUrlRequest(url))
                .retrieve()
                .bodyToMono(PythonUrlResponse.class)
                .block();
    }


}
