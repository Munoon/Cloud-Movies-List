package com.movies.list

import com.graphql.spring.boot.test.GraphQLTestAutoConfiguration
import com.graphql.spring.boot.test.GraphQLTestTemplate
import com.movies.common.AuthorizedUser
import com.movies.common.user.User
import com.movies.list.utils.GraphQLRestTemplate
import com.movies.list.utils.TestUtils
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.cache.CacheManager
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
import org.springframework.test.web.servlet.request.RequestPostProcessor
import java.util.*
import org.mockito.Mockito.`when` as mockWhen

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class AbstractTest {
    @Autowired
    private lateinit var mongoTemplate: MongoTemplate

    @Autowired
    private lateinit var cacheManager: CacheManager

    @MockBean
    @Qualifier("jwtTokenStore")
    private lateinit var tokenStore: TokenStore

    @BeforeEach
    fun clearDb() {
        mongoTemplate.db.drop();
        cacheManager.cacheNames.forEach { cacheManager.getCache(it)!!.clear() }
    }

    protected fun getAccessToken(user: User = TestUtils.DEFAULT_USER, oAuth2Request: OAuth2Request = TestUtils.DEFAULT_OAUTH_REQUEST): OAuth2AccessToken {
        val authUser = AuthorizedUser(User(user))
        val token = UsernamePasswordAuthenticationToken(authUser, "N/A", user.roles)
        val authentication = OAuth2Authentication(oAuth2Request, token)

        val accessToken = DefaultOAuth2AccessToken(UUID.randomUUID().toString())
        mockWhen(tokenStore.readAccessToken(accessToken.value)).thenReturn(accessToken)
        mockWhen(tokenStore.readAuthentication(accessToken)).thenReturn(authentication)
        return accessToken
    }

    protected fun authUser(user: User = TestUtils.DEFAULT_USER, oAuth2Request: OAuth2Request = TestUtils.DEFAULT_OAUTH_REQUEST): RequestPostProcessor {
        val accessToken = getAccessToken(user, oAuth2Request)
        return RequestPostProcessor {
            it.addHeader("Authorization", "${OAuth2AccessToken.BEARER_TYPE} ${accessToken.value}")
            it
        }
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