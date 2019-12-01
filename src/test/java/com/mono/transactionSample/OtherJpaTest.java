package com.mono.transactionSample;

import com.mono.transactionSample.model.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootApplication(
        scanBasePackages={"com.mono.transactionSample"})
@ActiveProfiles("nonXA-test")
public class OtherJpaTest {

    @Autowired
    UserService userService;

    @Test
    public void findAll() throws Exception {
        userService.findAll();
    }
}
