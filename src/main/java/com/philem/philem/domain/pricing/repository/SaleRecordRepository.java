package com.philem.philem.domain.pricing.repository;

import com.philem.philem.domain.pricing.entity.SaleRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRecordRepository extends JpaRepository<SaleRecord, Long> {
}
