package com.levp.catsandmath.presentation.core

import com.levp.catsandmath.data.remote.dto.NumFact
import retrofit2.Response

sealed class UiStateNF {
    object Loading : UiStateNF()
    data class Success(
        val numFactResponse: Response<NumFact>
    ) : UiStateNF()

    data class Error(val message: String) : UiStateNF()
}