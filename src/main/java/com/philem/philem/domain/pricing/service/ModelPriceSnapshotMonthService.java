package com.philem.philem.domain.pricing.service;

import com.philem.philem.domain.pricing.dto.BundleCompareRequest;
import com.philem.philem.domain.pricing.dto.BundleCompareResponse;
import com.philem.philem.domain.pricing.dto.ModelPriceSnapshotMonthResponse;
import com.philem.philem.domain.pricing.dto.PriceCompareResponse;
import com.philem.philem.domain.pricing.repository.ModelPriceSnapshotMonthRepository;
import com.philem.philem.domain.pricing.util.ModelPriceSnapshotMonthMapper;
import com.philem.philem.domain.shared.enums.ConditionType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@Service
@RequiredArgsConstructor
public class ModelPriceSnapshotMonthService {
    private final ModelPriceSnapshotMonthRepository modelPriceSnapshotMonthRepository;
    private final JdbcTemplate jdbcTemplate;

    @Transactional(readOnly = true)
    public List<ModelPriceSnapshotMonthResponse> getLastNMonths(Long modelId, int months) {
        Pageable pageable = PageRequest.of(0, months);

        return modelPriceSnapshotMonthRepository
                .findLatestNMonthsAllConditions(modelId, months)
                .stream()
                .map(ModelPriceSnapshotMonthMapper::toModelPriceSnapshotMonthResponse)
                .toList();
    }

    public PriceCompareResponse compareSingle(Long modelId, ConditionType condition, Long inputPrice) {

        var latestOpt = modelPriceSnapshotMonthRepository.findLatestByModelIdAndCondition(modelId, condition);
        if (latestOpt.isEmpty()) {
            return new PriceCompareResponse(
                    modelId,
                    condition,
                    inputPrice,
                    false,
                    "No snapshot found for this model/condition",
                    null,
                    null,
                    null,
                    null,
                    null,
                    "UNAVAILABLE"
            );
        }

        var latest = latestOpt.get();

        Integer refYear = latest.getSoldYear();     // ✅ 엔티티 getter가 다르면 여기만 수정
        Integer refMonth = latest.getSoldMonth();   // ✅
        Long refAvgPrice = latest.getAvgPrice();    // ✅

        long diffPrice = inputPrice - refAvgPrice;
        Double percentVsRef = (refAvgPrice == 0) ? null : (diffPrice * 100.0) / refAvgPrice;
        String direction = directionFromDiff(diffPrice);

        return new PriceCompareResponse(
                modelId,
                condition,
                inputPrice,
                true,
                null,
                refYear,
                refMonth,
                refAvgPrice,
                diffPrice,
                percentVsRef,
                direction
        );
    }

    public BundleCompareResponse compareBundle(BundleCompareRequest request) {

        long inputBundlePrice = request.bundle_price();

        long refSum = 0L;
        Integer bundleRefYear = null;
        Integer bundleRefMonth = null;

        var breakdowns = new ArrayList<BundleCompareResponse.ItemBreakdown>();

        // 하나라도 snapshot 없으면 available=false로 만들고 reason에 누락을 적기
        boolean available = true;
        StringJoiner missing = new StringJoiner(", ");

        for (var item : request.items()) {
            Long modelId = item.model_id();
            ConditionType condition = item.condition();

            var latestOpt = modelPriceSnapshotMonthRepository.findLatestByModelIdAndCondition(modelId, condition);
            if (latestOpt.isEmpty()) {
                available = false;
                missing.add("model_id=" + modelId + ", condition=" + condition);
                continue;
            }

            var latest = latestOpt.get();

            Integer y = latest.getSoldYear();     // ✅ 엔티티 getter가 다르면 여기만 수정
            Integer m = latest.getSoldMonth();   // ✅
            Long avg = latest.getAvgPrice();     // ✅

            refSum += avg;

            // 번들 ref_year/ref_month = 아이템 중 가장 최신(max)
            if (bundleRefYear == null || y > bundleRefYear || (y.equals(bundleRefYear) && m > bundleRefMonth)) {
                bundleRefYear = y;
                bundleRefMonth = m;
            }

            breakdowns.add(new BundleCompareResponse.ItemBreakdown(
                    modelId,
                    condition,
                    y,
                    m,
                    avg
            ));
        }

        if (!available) {
            // 누락이 있으면 비교 자체를 '불가'로 처리 (필드들은 null로)
            return new BundleCompareResponse(
                    inputBundlePrice,
                    false,
                    "Missing snapshot(s): " + missing,
                    null,
                    null,
                    null,
                    null,
                    null,
                    "UNAVAILABLE",
                    breakdowns
            );
        }

        long diffPrice = inputBundlePrice - refSum;
        Double percentVsRef = (refSum == 0) ? null : (diffPrice * 100.0) / refSum;
        String direction = directionFromDiff(diffPrice);

        return new BundleCompareResponse(
                inputBundlePrice,
                true,
                null,
                bundleRefYear,
                bundleRefMonth,
                refSum,
                diffPrice,
                percentVsRef,
                direction,
                breakdowns
        );
    }

    private static String directionFromDiff(long diffPrice) {
        if (diffPrice < 0) return "CHEAPER";
        if (diffPrice > 0) return "EXPENSIVE";
        return "SAME";
    }

}
