package com.mono.transactionSample.model;

import java.util.List;

public interface UserService {

    void addMultiTenantCode(String tenantCode, String userId, String userName, String password ) throws Exception;

    List<MultiTenantCodeCtrl> findAll();
}
