package com.movies.list.utils

import com.movies.common.user.User
import com.movies.common.user.UserRoles
import org.springframework.security.oauth2.provider.OAuth2Request
import java.time.LocalDateTime
import java.util.*

object TestUtils {
    const val DEFAULT_CLIENT_ID = "testClient"
    val DEFAULT_OAUTH_REQUEST = OAuth2Request(
            Collections.singletonMap("client_id", DEFAULT_CLIENT_ID),
            DEFAULT_CLIENT_ID, emptySet(),
            true, setOf("user_info"), emptySet(), "http://localhost:8080", emptySet(), emptyMap())
    val DEFAULT_USER = User(100, "Nikita", "Ivchenko", "munoongg@gmail.com", "{noop}pass", LocalDateTime.now(), setOf(UserRoles.ROLE_USER, UserRoles.ROLE_ADMIN))
}