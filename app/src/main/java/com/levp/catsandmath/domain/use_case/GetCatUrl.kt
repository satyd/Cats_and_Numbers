package com.levp.catsandmath.domain.use_case

import com.levp.catsandmath.core.Resource
import com.levp.catsandmath.data.remote.dto.CatImageUrl
import com.levp.catsandmath.domain.Repository
import kotlinx.coroutines.flow.Flow

class GetCatUrl(
    private val repository: Repository
) {

    operator fun invoke(): Resource<CatImageUrl>{
        return repository.getCatUrl()
    }
}