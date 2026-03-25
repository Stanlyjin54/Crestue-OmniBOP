package com.kuafu.mis_app_backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(properties = {"spring.profiles.active=test"})
@ActiveProfiles("test")
class MisAppBackendApplicationTests {

    @Test
    void contextLoads() {
    }

}






