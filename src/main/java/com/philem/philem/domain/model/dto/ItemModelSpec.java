package com.philem.philem.domain.model.dto;

import com.philem.philem.domain.model.entity.BrandType;
import com.philem.philem.domain.model.entity.CameraType;
import com.philem.philem.domain.model.entity.MountType;
import com.philem.philem.domain.model.entity.SensorFormat;
import com.philem.philem.domain.shared.enums.UnitType;
import lombok.Value;

@Value
public class ItemModelSpec {
    BrandType brand;
    UnitType unitType;
    CameraType cameraType;
    MountType mount;
    SensorFormat sensorFormat;
}