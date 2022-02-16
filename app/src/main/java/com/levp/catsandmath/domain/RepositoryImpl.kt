package com.levp.catsandmath.domain

import androidx.lifecycle.viewModelScope
import com.levp.catsandmath.core.Resource
import com.levp.catsandmath.data.remote.dto.CatImageUrl
import com.levp.catsandmath.data.remote.dto.NumFact
import com.levp.catsandmath.presentation.core.UiStateNF
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class RepositoryImpl : Repository {

    override fun getCatUrl(): Resource<CatImageUrl> {
        TODO("Not yet implemented")
    }

    override fun getNumFact(type: String): Resource<NumFact> {
        TODO("Not yet implemented")
    }

}