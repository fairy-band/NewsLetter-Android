package com.nexters.knownknowns.presentation.feature.detail

import androidx.lifecycle.ViewModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class DetailViewModel : ViewModel() {
    init {
        println("ViewModel이 생성되었어요.")
    }

    fun temp() {
        println("왜 안 호출 돼~~")
    }

    override fun onCleared() {
        super.onCleared()

        println("ViewModel이 소멸돼요~")
    }
}