package com.philem.philem.domain.model.repository;

import com.philem.philem.domain.model.dto.ItemModelSpec;
import com.philem.philem.domain.model.entity.BrandType;
import com.philem.philem.domain.model.entity.CameraType;
import com.philem.philem.domain.model.entity.MountType;
import com.philem.philem.domain.model.entity.SensorFormat;
import com.philem.philem.domain.shared.enums.UnitType;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.*;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ItemModelSpecRepositoryImpl implements ItemModelSpecRepository {
    private final NamedParameterJdbcTemplate jdbc;

    @Override
    public ItemModelSpec getSpecOrThrow(Long itemModelId) {
        String sql = """
            SELECT brand, unit_type, camera_type, mount, sensor_format
            FROM itemModel
            WHERE id = :id
        """;
        var p = new MapSqlParameterSource().addValue("id", itemModelId);

        return jdbc.query(sql, p, rs -> {
            if (!rs.next()) throw new IllegalArgumentException("ItemModel not found: " + itemModelId);
            return new ItemModelSpec(
                    BrandType.valueOf(rs.getString("brand")),
                    UnitType.valueOf(rs.getString("unit_type")),
                    CameraType.valueOf(rs.getString("camera_type")),
                    MountType.valueOf(normalizeMount(rs.getString("mount"))),
                    parseSensorFormat(rs.getString("sensor_format"))
            );
        });
    }

    private static String normalizeMount(String v) {
        if (v == null) return null;
        return v.equalsIgnoreCase("EF-M") ? "EF_M" : v;
    }

    private static SensorFormat parseSensorFormat(String dbValue) {
        if (dbValue == null) return null;
        for (SensorFormat sf : SensorFormat.values()) {
            if (sf.label().equalsIgnoreCase(dbValue) || sf.name().equalsIgnoreCase(dbValue)) return sf;
        }
        throw new IllegalArgumentException("Unknown sensor_format: " + dbValue);
    }
}
