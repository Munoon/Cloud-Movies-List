package com.movies.user;

import com.movies.common.user.User;
import io.restassured.config.EncoderConfig;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.config.RestAssuredMockMvcConfig;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;

import java.nio.charset.StandardCharsets;

import static com.movies.user.user.UserTestData.DEFAULT_USER;
import static com.movies.user.util.TestUtils.DEFAULT_OAUTH_REQUEST;
import static com.movies.user.util.TestUtils.addAuthentication;

@SpringBootTest
@RunWith(SpringRunner.class)
@Sql(scripts = { "classpath:db/data.sql" })
public abstract class BaseContractTest {
    @Autowired
    private WebApplicationContext context;

    @Before
    public void setup() {
        MockMvc mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .addFilter((request, response, chain) -> {
                    HttpServletRequest httpRequest = (HttpServletRequest) request;
                    String authorization = httpRequest.getHeader("Authorization");
                    if (authorization != null && authorization.equals("bearer DEFAULT_USER")) {
                        addAuthentication(DEFAULT_OAUTH_REQUEST, new User(DEFAULT_USER));
                    }
                    chain.doFilter(request, response);
                })
                .build();
        RestAssuredMockMvc.mockMvc(mockMvc);
        RestAssuredMockMvc.config = new RestAssuredMockMvcConfig()
                .encoderConfig(new EncoderConfig(StandardCharsets.UTF_8.name(), StandardCharsets.UTF_8.name()));
    }
}
