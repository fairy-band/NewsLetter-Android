package com.fairyband.soak.data.datasource

import com.fairyband.soak.data.local.auth.AuthDataStore
import com.fairyband.soak.data.model.request.RegisterRequest
import com.fairyband.soak.data.model.response.LoginResponse
import com.fairyband.soak.data.remote.service.AuthService
import kotlinx.coroutines.flow.first
import okio.IOException
import org.koin.core.annotation.Single
import retrofit2.HttpException

@Single
class AuthDataSource(
    private val authDataStore: AuthDataStore,
    private val authService: AuthService,
) {
    suspend fun getUserId(): Long {
        return authDataStore.userId.first() ?: loginUser().id
    }

    private suspend fun setUserId(id: Long) {
        authDataStore.setUserId(id)
    }

    suspend fun getDeviceToken(): String = authDataStore.deviceToken.first()

    suspend fun loginUser(): LoginResponse {
        val response = try {
            authService.loginUser(
                deviceToken = getDeviceToken()
            )
        } catch (e: HttpException) {
            // 등록되지 않은 사용자에 대한 처리
            // TODO: 지금은 따로 상세한 에러 구분 없이 모두 500으로 내려온다.
            authService.registerUser(
                RegisterRequest(deviceToken = getDeviceToken())
            )
        }

        setUserId(response.id)

        return response
    }
}
