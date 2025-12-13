package com.philem.philem.domain.model.repository;

import com.philem.philem.domain.model.entity.ItemModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemModelRepository extends JpaRepository<ItemModel, Long> {
}
