package com.philem.philem.domain.search.dto;

import com.philem.philem.domain.model.entity.BrandType;
import com.philem.philem.domain.model.entity.CameraType;
import com.philem.philem.domain.model.entity.MountType;
import com.philem.philem.domain.model.entity.SensorFormat;
import com.philem.philem.domain.search.entity.SearchMode;
import com.philem.philem.domain.shared.enums.ConditionType;
import com.philem.philem.domain.shared.enums.UnitType;
import lombok.Data;

@Data
public class ListingSearchRequest {
    // mode
    private SearchMode mode = SearchMode.FILTER;

    // filter
    private ConditionType condition;
    private Integer minPrice;
    private Integer maxPrice;
    private BrandType brand;
    private UnitType unitType;
    private CameraType cameraType;
    private MountType mount;
    private SensorFormat sensorFormat;

    // preset
    private Long bodyModelId;
    private Long lensModelId;
    private String presetLensBrand = "BODY_BRAND"; // BODY_BRAND or BrandType name

    // paging
    private Integer page = 0;
    private Integer size = 20;
}