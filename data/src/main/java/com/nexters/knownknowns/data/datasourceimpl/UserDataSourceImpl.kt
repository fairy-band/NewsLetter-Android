package com.nexters.knownknowns.data.datasourceimpl

import com.nexters.knownknowns.data.datasource.UserDataSource
import com.nexters.knownknowns.data.model.request.UserInfoRequest
import com.nexters.knownknowns.data.remote.service.UserService
import org.koin.core.annotation.Single

@Single
internal class UserDataSourceImpl(
    private val userService: UserService,
) : UserDataSource {
    override suspend fun putUserInfo(request: UserInfoRequest) {
        userService.putUserInfo(
            userId = 4,
            body = request
        )
    }
}
