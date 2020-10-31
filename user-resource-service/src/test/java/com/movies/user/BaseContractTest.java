package com.movies.user;

import com.movies.user.controller.AbstractWebTest;
import com.movies.user.user.UserTestData;
import com.movies.user.util.TestUtils;
import io.restassured.config.EncoderConfig;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.config.RestAssuredMockMvcConfig;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

@RunWith(SpringRunner.class)
public abstract class BaseContractTest extends AbstractWebTest {
    @Before
    public void setup() {
        getAccessToken(UserTestData.DEFAULT_USER, TestUtils.DEFAULT_OAUTH_REQUEST, "DEFAULT_USER");
        MockMvc mockMvc = getNewMockMvc();
        RestAssuredMockMvc.mockMvc(mockMvc);
        RestAssuredMockMvc.config = new RestAssuredMockMvcConfig()
                .encoderConfig(new EncoderConfig(StandardCharsets.UTF_8.name(), StandardCharsets.UTF_8.name()));
    }
}
