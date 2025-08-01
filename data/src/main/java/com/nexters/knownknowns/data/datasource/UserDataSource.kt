package com.nexters.knownknowns.data.datasource

import com.nexters.knownknowns.data.model.request.UserInfoRequest

internal interface UserDataSource {
    suspend fun putUserInfo(request: UserInfoRequest)
}
