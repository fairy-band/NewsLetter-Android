package com.nexters.knownknowns.data.datasource

import com.nexters.knownknowns.data.model.request.UserInfoRequest
import com.nexters.knownknowns.data.remote.service.UserService

internal class UserDataSource(
    private val userService: UserService,
) {
    suspend fun putUserInfo(
        userId: Long,
        request: UserInfoRequest
    ) {

        userService.putUserInfo(
            userId = userId,
            body = request
        )
    }
}
