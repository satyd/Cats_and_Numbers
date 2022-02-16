package com.levp.catsandmath.presentation.core

import com.levp.catsandmath.data.remote.dto.CatImageUrl

data class CatState(
    val item:String = "https://http.cat/404",
    val isLoading:Boolean = false
)
