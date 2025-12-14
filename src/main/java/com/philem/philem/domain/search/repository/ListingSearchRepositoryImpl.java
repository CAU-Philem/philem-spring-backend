package com.philem.philem.domain.search.repository;

import com.philem.philem.domain.model.entity.BrandType;
import com.philem.philem.domain.model.entity.CameraType;
import com.philem.philem.domain.model.entity.MountType;
import com.philem.philem.domain.model.entity.SensorFormat;
import com.philem.philem.domain.search.dto.*;
import com.philem.philem.domain.shared.enums.*;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.*;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ListingSearchRepositoryImpl implements ListingSearchRepository {

    private final NamedParameterJdbcTemplate jdbc;

    @Override
    public ListingSearchResponse search(ListingSearchCondition c) {
        StringBuilder where = new StringBuilder(" WHERE 1=1 \n");
        MapSqlParameterSource p = new MapSqlParameterSource();

        where.append(" AND (l.status <> 'SoldOut') \n");
        // --------------------------
        // ItemModel 기반 필터
        // --------------------------
        if (c.getBrand() != null) {
            where.append(" AND m.brand = :brand \n");
            p.addValue("brand", c.getBrand().name());
        } else if (c.getBrandIn() != null && !c.getBrandIn().isEmpty()) {
            where.append(" AND m.brand IN (:brandIn) \n");
            p.addValue("brandIn", c.getBrandIn().stream().map(Enum::name).toList());
        }

        if (c.getUnitType() != null) {
            where.append(" AND m.unit_type = :unitType \n");
            p.addValue("unitType", c.getUnitType().name());
        }

        if (c.getCameraType() != null) {
            where.append(" AND m.camera_type = :cameraType \n");
            p.addValue("cameraType", c.getCameraType().name());
        }

        if (c.getMount() != null) {
            // DB가 'EF-M'을 쓰면 enum EF_M을 변환
            where.append(" AND m.mount = :mount \n");
            p.addValue("mount", mountToDbValue(c.getMount()));
        }

        if (c.getSensorFormat() != null) {
            // DB에 label 저장("Full Frame") 가정. enum key 저장이면 .name()으로 바꿔.
            where.append(" AND m.sensor_format = :sensorFormat \n");
            p.addValue("sensorFormat", c.getSensorFormat().label());
        }

        // --------------------------
        // ListingItem 기반 필터
        // --------------------------
        if (c.getCondition() != null) {
            where.append(" AND li.condition = :cond \n");
            p.addValue("cond", c.getCondition().name());
        }

        if (c.getMinPrice() != null) {
            where.append(" AND li.price >= :minPrice \n");
            p.addValue("minPrice", c.getMinPrice());
        }

        if (c.getMaxPrice() != null) {
            where.append(" AND li.price <= :maxPrice \n");
            p.addValue("maxPrice", c.getMaxPrice());
        }

        // --------------------------
        // COUNT
        // --------------------------
        String countSql = """
            SELECT COUNT(*)
            FROM ListingItem li
            JOIN listing l ON l.seq = li.listing_id
            JOIN itemModel m ON m.id = li.model_id
        """ + where;

        long total = jdbc.queryForObject(countSql, p, Long.class);

        // paging
        int size = Math.max(1, c.getSize());
        int page = Math.max(0, c.getPage());
        int offset = page * size;
        p.addValue("limit", size);
        p.addValue("offset", offset);

        // --------------------------
        // MAIN QUERY (판매량 정렬)
        // --------------------------
        String sql = """
            SELECT
              li.id            AS listing_item_id,
              li.model_id      AS model_id,
              m.name           AS model_name,
              m.brand          AS brand,
              m.unit_type      AS unit_type,
              m.camera_type    AS camera_type,
              m.mount          AS mount,
              m.sensor_format  AS sensor_format,
              CASE
                WHEN li.price_type = 'BUNDLE_SHARED' THEN l.price
                ELSE li.price
              END AS display_price,
              li.price_type    AS price_type,
              li.condition     AS `condition`,
              li.post_url      AS post_url,
              l.thumbnail_url  AS thumbnail_url,
              COALESCE(sr.sales_count, 0) AS sales_count
            FROM ListingItem li
            JOIN listing l ON l.seq = li.listing_id
            JOIN itemModel m ON m.id = li.model_id
            LEFT JOIN (
              SELECT model_id, COUNT(*) AS sales_count
              FROM SaleRecord
              GROUP BY model_id
            ) sr ON sr.model_id = li.model_id
        """ + where + """
            ORDER BY sales_count DESC, li.id DESC
            LIMIT :limit OFFSET :offset
        """;

        List<ListingSearchItem> items = jdbc.query(sql, p, (rs, rowNum) -> {
            return ListingSearchItem.builder()
                    .listingItemId(rs.getLong("listing_item_id"))
                    .modelId(rs.getLong("model_id"))
                    .modelName(rs.getString("model_name"))
                    .brand(BrandType.valueOf(rs.getString("brand")))
                    .unitType(UnitType.valueOf(rs.getString("unit_type")))
                    .cameraType(CameraType.valueOf(rs.getString("camera_type")))
                    .mount(dbValueToMount(rs.getString("mount")))
                    .sensorFormat(dbValueToSensorFormat(rs.getString("sensor_format")))
                    .price(rs.getInt("display_price"))
                    .condition(rs.getString("condition") == null ? null : ConditionType.valueOf(rs.getString("condition")))
                    .postUrl(rs.getString("post_url"))
                    .thumbnailUrl(rs.getString("thumbnail_url"))
                    .priceType(
                            rs.getString("price_type") == null
                                    ? null
                                    : PriceType.valueOf(rs.getString("price_type"))
                    )
                    .salesCount(rs.getLong("sales_count"))
                    .build();
        });

        return ListingSearchResponse.builder()
                .items(items)
                .page(page)
                .size(size)
                .total(total)
                .build();
    }

    private static LocalDateTime toLocalDateTime(Timestamp ts) {
        return ts == null ? null : ts.toLocalDateTime();
    }

    private static String mountToDbValue(MountType m) {
        // enum EF_M <-> DB 'EF-M'
        return (m == MountType.EF_M) ? "EF-M" : m.name();
    }

    private static MountType dbValueToMount(String v) {
        if (v == null) return null;
        return v.equalsIgnoreCase("EF-M") ? MountType.EF_M : MountType.valueOf(v);
    }

    private static SensorFormat dbValueToSensorFormat(String v) {
        if (v == null) return null;
        for (SensorFormat sf : SensorFormat.values()) {
            if (sf.label().equalsIgnoreCase(v) || sf.name().equalsIgnoreCase(v)) return sf;
        }
        throw new IllegalArgumentException("Unknown sensor_format in DB: " + v);
    }
}
