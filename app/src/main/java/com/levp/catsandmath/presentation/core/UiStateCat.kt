package com.levp.catsandmath.presentation.core

import com.levp.catsandmath.data.remote.dto.CatImageUrl
import com.levp.catsandmath.data.remote.dto.NumFact
import retrofit2.Response

sealed class UiStateCat {
    object Loading : UiStateCat()
    data class Success(
        val catResponse: Response<CatImageUrl>
    ) : UiStateCat()

    data class Error(val message: String) : UiStateCat()
}