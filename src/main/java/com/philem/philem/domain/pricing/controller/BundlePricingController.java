package com.philem.philem.domain.pricing.controller;

import com.philem.philem.domain.pricing.dto.BundleCompareRequest;
import com.philem.philem.domain.pricing.dto.BundleCompareResponse;
import com.philem.philem.domain.pricing.service.ModelPriceSnapshotMonthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bundles")
public class BundlePricingController {

    private final ModelPriceSnapshotMonthService modelPriceSnapshotMonthService;

    // POST /bundles/compare
    @PostMapping("/compare")
    public BundleCompareResponse compareBundle(@RequestBody BundleCompareRequest request) {

        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "request body is required");
        }
        if (request.bundle_price() == null || request.bundle_price() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "bundle_price must be > 0");
        }
        if (request.items() == null || request.items().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "items must not be empty");
        }

        // model_id null 같은 것만 체크 (condition은 enum이라 자동 validation 성격)
        for (var item : request.items()) {
            if (item.model_id() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "model_id is required for every item");
            }
            if (item.condition() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "condition is required for every item");
            }
        }

        return modelPriceSnapshotMonthService.compareBundle(request);
    }
}
