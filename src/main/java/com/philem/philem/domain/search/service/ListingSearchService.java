package com.philem.philem.domain.search.service;

// ListingSearchService.java
import com.philem.philem.domain.model.dto.ItemModelSpec;
import com.philem.philem.domain.model.entity.BrandType;
import com.philem.philem.domain.model.repository.ItemModelRepository;
import com.philem.philem.domain.model.repository.ItemModelSpecRepository;
import com.philem.philem.domain.search.dto.ListingSearchCondition;
import com.philem.philem.domain.search.dto.ListingSearchRequest;
import com.philem.philem.domain.search.dto.ListingSearchResponse;
import com.philem.philem.domain.search.repository.ListingSearchRepository;
import com.philem.philem.domain.shared.enums.UnitType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ListingSearchService {

    private final ItemModelSpecRepository itemModelSpecRepository;
    private final ListingSearchRepository listingSearchRepository;

    public ListingSearchResponse search(ListingSearchRequest req) {
        ListingSearchCondition c = switch (req.getMode()) {
            case FILTER -> fromFilter(req);
            case BODY_TO_LENS -> bodyToLens(req);
            case LENS_TO_BODY -> lensToBody(req);
        };
        return listingSearchRepository.search(c);
    }

    private ListingSearchCondition bodyToLens(ListingSearchRequest r) {
        if (r.getBodyModelId() == null) throw new IllegalArgumentException("BODY_TO_LENS requires bodyModelId");

        ItemModelSpec body = itemModelSpecRepository.getSpecOrThrow(r.getBodyModelId());

        // 렌즈 브랜드: (바디브랜드 or 서드파티) 택1
        BrandType picked = resolvePresetLensBrand(r.getPresetLensBrand(), body.getBrand());

        return ListingSearchCondition.builder()
                .condition(r.getCondition())
                .minPrice(r.getMinPrice())
                .maxPrice(r.getMaxPrice())

                .unitType(UnitType.LENS)
                .cameraType(body.getCameraType())
                .mount(body.getMount())
                .sensorFormat(body.getSensorFormat())

                // 사용자가 brand 직접 지정하면 그걸로 우선
                .brand(r.getBrand())
                .brandIn(r.getBrand() == null ? List.of(picked) : null)

                .page(nvl(r.getPage(), 0))
                .size(nvl(r.getSize(), 20))
                .build();
    }

    private ListingSearchCondition lensToBody(ListingSearchRequest r) {
        if (r.getLensModelId() == null) throw new IllegalArgumentException("LENS_TO_BODY requires lensModelId");

        ItemModelSpec lens = itemModelSpecRepository.getSpecOrThrow(r.getLensModelId());

        return ListingSearchCondition.builder()
                .condition(r.getCondition())
                .minPrice(r.getMinPrice())
                .maxPrice(r.getMaxPrice())

                .unitType(UnitType.BODY)
                .cameraType(lens.getCameraType())
                .mount(lens.getMount())
                .sensorFormat(lens.getSensorFormat())

                .brand(r.getBrand())
                .page(nvl(r.getPage(), 0))
                .size(nvl(r.getSize(), 20))
                .build();
    }

    private ListingSearchCondition fromFilter(ListingSearchRequest r) {
        return ListingSearchCondition.builder()
                .condition(r.getCondition())
                .minPrice(r.getMinPrice())
                .maxPrice(r.getMaxPrice())
                .brand(r.getBrand())
                .unitType(r.getUnitType())
                .cameraType(r.getCameraType())
                .mount(r.getMount())
                .sensorFormat(r.getSensorFormat())
                .page(nvl(r.getPage(), 0))
                .size(nvl(r.getSize(), 20))
                .build();
    }

    private BrandType resolvePresetLensBrand(String preset, BrandType bodyBrand) {
        if (preset == null || preset.isBlank() || preset.equalsIgnoreCase("BODY_BRAND")) return bodyBrand;
        return BrandType.valueOf(preset.trim().toUpperCase());
    }

    private int nvl(Integer v, int d) { return v == null ? d : v; }
}
