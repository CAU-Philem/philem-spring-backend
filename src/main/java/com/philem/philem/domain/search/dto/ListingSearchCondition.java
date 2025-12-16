package com.philem.philem.domain.search.dto;

import com.philem.philem.domain.model.entity.BrandType;
import com.philem.philem.domain.model.entity.CameraType;
import com.philem.philem.domain.model.entity.MountType;
import com.philem.philem.domain.model.entity.SensorFormat;
import com.philem.philem.domain.shared.enums.*;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class ListingSearchCondition {

    // ListingItem filters
    ConditionType condition; // A/B/C
    Integer minPrice;
    Integer maxPrice;

    // ItemModel filters
    BrandType brand;          // 단일 브랜드
    List<BrandType> brandIn;  // 프리셋용 (택1이지만 IN으로 처리)

    UnitType unitType;        // BODY/LENS
    CameraType cameraType;    // MIRRORLESS/DSLR
    MountType mount;          // E, Z, F, RF, EF, EF_M, X, G
    SensorFormat sensorFormat;// FULL_FRAME, APS_C, ...

    // paging
    int page;
    int size;
}
