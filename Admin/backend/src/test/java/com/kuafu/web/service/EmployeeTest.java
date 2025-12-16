package com.kuafuweb.web.service;

import com.kuafuweb.MisAppBackendApplication;
import com.kuafuweb.common.config.AppConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = {MisAppBackendApplication.class})
@RunWith(SpringRunner.class)
public class EmployeeTest {


    @Test
    public void test1() {

    }

    @Test
    public void test2() {

    }

    @Test
    public void test3() {
        System.out.println(AppConfig.getProfile());
    }
}





