package com.mono.transactionSample.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

@Service
public class McdUserService implements UserService {

    @Autowired
    private MultiTenantCodeCtrlRepository repository;

    @Override
    @Transactional
    public void addMultiTenantCode(String tenantCode, String userId, String userName, String password) throws Exception {
        Assert.notNull(tenantCode, "TenantCode can not be null");
        Assert.notNull(userId, "UserId can not be null");

        MultiTenantCodeCtrl userEntity = new MultiTenantCodeCtrl(tenantCode, userId);
        userEntity.setUserName(userName);
        userEntity.setPassword(password);

        repository.saveAndFlush(userEntity);
    }

    @Override
    public List<MultiTenantCodeCtrl> findAll() {
        return (repository.findAll());
    }
}
