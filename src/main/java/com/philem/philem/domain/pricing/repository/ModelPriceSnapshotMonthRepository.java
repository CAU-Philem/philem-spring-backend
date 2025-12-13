package com.philem.philem.domain.pricing.repository;

import com.philem.philem.domain.pricing.entity.ModelPriceSnapshotMonth;
import com.philem.philem.domain.shared.enums.ConditionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ModelPriceSnapshotMonthRepository extends JpaRepository<ModelPriceSnapshotMonth, Long> {

    @Query(value = """
        SELECT s.*
        FROM model_price_snapshot_month s
        JOIN (
            SELECT sold_year, sold_month
            FROM model_price_snapshot_month
            WHERE model_id = :modelId
            GROUP BY sold_year, sold_month
            ORDER BY sold_year DESC, sold_month DESC
            LIMIT :months
        ) ym
          ON ym.sold_year = s.sold_year AND ym.sold_month = s.sold_month
        WHERE s.model_id = :modelId
        ORDER BY s.sold_year DESC, s.sold_month DESC, s.`condition` ASC
        """, nativeQuery = true)
    List<ModelPriceSnapshotMonth> findLatestNMonthsAllConditions(
            @Param("modelId") Long modelId,
            @Param("months") int months
    );

    Page<ModelPriceSnapshotMonth> findByItemModel_IdOrderBySoldYearDescSoldMonthDesc(
            Long itemModelId,
            Pageable pageable
    );

    Optional<ModelPriceSnapshotMonth> findTopByItemModelIdAndConditionOrderBySoldYearDescSoldMonthDesc(
            Long modelId,
            ConditionType condition
    );

    default Optional<ModelPriceSnapshotMonth> findLatestByModelIdAndCondition(Long modelId, ConditionType condition) {
        return findTopByItemModelIdAndConditionOrderBySoldYearDescSoldMonthDesc(modelId, condition);
    }

}
