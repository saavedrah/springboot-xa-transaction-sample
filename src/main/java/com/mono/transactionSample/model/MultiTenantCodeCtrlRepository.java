package com.mono.transactionSample.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MultiTenantCodeCtrlRepository extends JpaRepository<MultiTenantCodeCtrl, MultiTenantCodeCtrl.MultiTenantCodeCtrlPK> {

    MultiTenantCodeCtrl findByTenantCodeAndUserId(String tenantCode, String userId);
}
