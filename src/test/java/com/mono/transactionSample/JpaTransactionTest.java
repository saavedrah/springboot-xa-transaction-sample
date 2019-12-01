package com.mono.transactionSample;

import com.mono.transactionSample.model.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

@SpringBootApplication(
        scanBasePackages={"com.mono.transactionSample"})
@RunWith(SpringRunner.class)
@ActiveProfiles("nonXA-test")
@ContextConfiguration(classes={ContextConfigurationForTest.class})
@EnableAutoConfiguration
public class JpaTransactionTest {

    @Autowired
    UserService userService;

    @Test
    public void insertUserTest() throws Exception {

        userService.addMultiTenantCode("AUTOTEST", UUID.randomUUID().toString(), "user name", "password");
    }
}
