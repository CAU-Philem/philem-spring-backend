package com.philem.philem.domain.search.dto;

import com.philem.philem.domain.model.entity.BrandType;
import com.philem.philem.domain.model.entity.CameraType;
import com.philem.philem.domain.model.entity.MountType;
import com.philem.philem.domain.model.entity.SensorFormat;
import com.philem.philem.domain.shared.enums.ConditionType;
import com.philem.philem.domain.shared.enums.UnitType;
import lombok.Builder;
import lombok.Value;
import java.time.LocalDateTime;

@Value
@Builder
public class ListingSearchItem {
    Long listingItemId;
    Long modelId;
    String modelName;

    BrandType brand;
    UnitType unitType;
    CameraType cameraType;
    MountType mount;
    SensorFormat sensorFormat;

    Integer price;
    ConditionType condition;
    LocalDateTime updatedAt;
    String postUrl;
    String thumbnailUrl;

    Long salesCount; // 정렬 기준
}