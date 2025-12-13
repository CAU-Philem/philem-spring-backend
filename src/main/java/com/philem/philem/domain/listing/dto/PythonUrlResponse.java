package com.philem.philem.domain.listing.dto;

import java.util.List;

public record PythonUrlResponse(
        Long listing_id,
        String post_url,
        Boolean is_bundle,
        Integer bundle_index,
        Long bundle_total_price,
        List<PythonProcessedItem> items
) {
}
