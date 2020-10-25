package com.movies.list

import com.graphql.spring.boot.test.GraphQLTestAutoConfiguration
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter

@Import(value = [GraphQLTestAutoConfiguration::class, AbstractTest.TestConfiguration::class])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class AbstractTest {
    @Autowired
    private lateinit var mongoTemplate: MongoTemplate

    @BeforeEach
    fun clearDb() {
        mongoTemplate.db.drop();
    }

    @Configuration
    class TestConfiguration {
        @Bean
        fun jwtAccessTokenConverter() = JwtAccessTokenConverter()
    }
}