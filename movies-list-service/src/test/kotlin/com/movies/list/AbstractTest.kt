package com.movies.list

import com.graphql.spring.boot.test.GraphQLTestAutoConfiguration
import com.graphql.spring.boot.test.GraphQLTestTemplate
import com.movies.common.AuthorizedUser
import com.movies.common.user.User
import com.movies.list.utils.GraphQLRestTemplate
import com.movies.list.utils.TestUtils
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken
import org.springframework.security.oauth2.common.OAuth2AccessToken
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.security.oauth2.provider.OAuth2Request
import org.springframework.security.oauth2.provider.token.TokenStore
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter
import java.util.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class AbstractTest {
    @Autowired
    private lateinit var mongoTemplate: MongoTemplate

    @Autowired
    private lateinit var tokenStore: TokenStore

    @BeforeEach
    fun clearDb() {
        mongoTemplate.db.drop();
    }

    protected fun getAccessToken(user: User = TestUtils.DEFAULT_USER, oAuth2Request: OAuth2Request = TestUtils.DEFAULT_OAUTH_REQUEST): OAuth2AccessToken {
        val authUser = AuthorizedUser(User(user))
        val token = UsernamePasswordAuthenticationToken(authUser, "N/A", user.roles)
        val authentication = OAuth2Authentication(oAuth2Request, token)

        val accessToken = DefaultOAuth2AccessToken(UUID.randomUUID().toString())
        tokenStore.storeAccessToken(accessToken, authentication)
        return accessToken
    }

    @Configuration
    @Import(GraphQLTestAutoConfiguration::class)
    class TestConfiguration {
        @Bean
        fun jwtAccessTokenConverter() = JwtAccessTokenConverter()

        @Bean
        fun graphQLRestTemplate(graphQLTestTemplate: GraphQLTestTemplate) = GraphQLRestTemplate(graphQLTestTemplate)
    }
}