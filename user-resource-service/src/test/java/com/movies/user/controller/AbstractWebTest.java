package com.movies.user.controller;

import com.movies.user.AbstractTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;

public class AbstractWebTest extends AbstractTest {
    @Autowired
    private WebApplicationContext context;

    protected MockMvc mockMvc;

    @PostConstruct
    void setupMockMvc() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }
}
