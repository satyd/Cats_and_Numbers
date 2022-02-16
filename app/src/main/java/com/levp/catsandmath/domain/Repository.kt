package com.levp.catsandmath.domain

import com.levp.catsandmath.core.Resource
import com.levp.catsandmath.data.remote.dto.CatImageUrl
import com.levp.catsandmath.data.remote.dto.NumFact
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface Repository {

    fun getCatUrl(): Resource<CatImageUrl>

    fun getNumFact(type: String): Resource<NumFact>

}