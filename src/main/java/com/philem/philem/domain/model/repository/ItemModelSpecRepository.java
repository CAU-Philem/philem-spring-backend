package com.philem.philem.domain.model.repository;

import com.philem.philem.domain.model.dto.ItemModelSpec;

public interface ItemModelSpecRepository {
    ItemModelSpec getSpecOrThrow(Long itemModelId);
}
