package com.philem.philem.domain.pricing.controller;

import com.philem.philem.domain.pricing.dto.ModelPriceSnapshotMonthResponse;
import com.philem.philem.domain.pricing.dto.PriceCompareResponse;
import com.philem.philem.domain.pricing.service.ModelPriceSnapshotMonthService;
import com.philem.philem.domain.shared.enums.ConditionType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/models")
public class ModelPriceSnapshotMonthController {
    private final ModelPriceSnapshotMonthService modelPriceSnapshotMonthService;

    //모델 id와 원하는 snapshot 개수를 주면 snapshot을 반환함 (단점이: condition 불문하고 순 개수로 따져서, 24 하면 (2025.12 ~2024.12)
    //이렇게 반환될수도 있고, 어떤 달에는 B급 상품이 없으면 (2025.12 ~ 2023.09)이렇게 뜰수도 있음 -> 이 부분은 총 달 수로 수정할게요. 일단
    //월수로 query한다 생각하고 사용하세요.
    @GetMapping("/{modelId}/snapshots")
    public List<ModelPriceSnapshotMonthResponse> getSnapshots(
            @PathVariable Long modelId,
            @RequestParam(defaultValue = "36") int months){
        return modelPriceSnapshotMonthService.getLastNMonths(modelId, months);
    }

    //모델 id, 상태, 그리고 가격 정보를 주면, 이전 달 (혹은 제일 최근 판매 기록이 있는 달)의 평균 가격과 비교해서 반환해줌
    @GetMapping("/{modelId}/compare")
    public PriceCompareResponse compareToLatestAvg(
            @PathVariable Long modelId,
            @RequestParam ConditionType condition,
            @RequestParam Long price
    ) {
        if (price == null || price <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "price must be > 0");
        }
        return modelPriceSnapshotMonthService.compareSingle(modelId, condition, price);
    }
}
