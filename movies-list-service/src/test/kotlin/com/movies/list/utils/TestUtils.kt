package com.movies.list.utils

import com.movies.common.AuthorizedUser
import com.movies.common.user.User
import com.movies.common.user.UserRoles
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.security.oauth2.provider.OAuth2Request
import org.springframework.test.web.servlet.request.RequestPostProcessor
import java.time.LocalDateTime
import java.util.*

object TestUtils {
    const val DEFAULT_CLIENT_ID = "testClient"
    val DEFAULT_OAUTH_REQUEST = OAuth2Request(
            Collections.singletonMap("client_id", DEFAULT_CLIENT_ID),
            DEFAULT_CLIENT_ID, emptySet(),
            true, setOf("user_info"), emptySet(), "http://localhost:8080", emptySet(), emptyMap())
    val DEFAULT_USER = User(100, "Nikita", "Ivchenko", "munoongg@gmail.com", "{noop}pass", LocalDateTime.now(), setOf(UserRoles.ROLE_USER, UserRoles.ROLE_ADMIN))

    private fun addAuthentication(oAuth2Request: OAuth2Request, user: User) {
        val authentication = OAuth2Authentication(
                oAuth2Request,
                UsernamePasswordAuthenticationToken(AuthorizedUser(user), null, user.roles)
        )
        SecurityContextHolder.getContext().authentication = authentication
    }

    fun customUserAndClient(oAuth2Request: OAuth2Request, user: User): RequestPostProcessor {
        addAuthentication(oAuth2Request, user)
        return RequestPostProcessor { request: MockHttpServletRequest? -> request!! }
    }


    fun customUser(user: User): RequestPostProcessor {
        return customUserAndClient(DEFAULT_OAUTH_REQUEST, user)
    }

    fun defaultUser(): RequestPostProcessor {
        return customUser(DEFAULT_USER)
    }
}