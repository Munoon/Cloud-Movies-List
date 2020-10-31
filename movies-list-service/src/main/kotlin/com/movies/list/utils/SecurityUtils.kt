package com.movies.list.utils

import com.movies.common.AuthorizedUser
import com.movies.common.user.UserTo
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException

object SecurityUtils {
    private fun safeGet(): AuthorizedUser? {
        val auth = SecurityContextHolder.getContext().authentication
                ?: return null
        val principal = auth.principal
        return if (principal is AuthorizedUser) principal else null
    }

    fun getAuthorizedUser(): AuthorizedUser =
        safeGet() ?: throw UnauthorizedUserException("No authorized user found")

    fun authUser(): UserTo = getAuthorizedUser().userTo

    fun authUserId(): Int = getAuthorizedUser().id

    fun authUserIdOrAnonymous() = safeGet()?.id?.toString() ?: "[anonymous]"
}