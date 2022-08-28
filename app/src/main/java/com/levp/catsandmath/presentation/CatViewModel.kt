package com.levp.catsandmath.presentation

import android.graphics.Bitmap
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.levp.catsandmath.core.Resource
import com.levp.catsandmath.core.buildCatApi
import com.levp.catsandmath.data.remote.CatApi
import com.levp.catsandmath.data.remote.dto.CatImageUrl
import com.levp.catsandmath.domain.use_case.GetCatUrl
import com.levp.catsandmath.presentation.core.BaseViewModel
import com.levp.catsandmath.presentation.core.CatState
import com.levp.catsandmath.presentation.core.UiStateCat
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

private val BASE_URL_CAT: String = "https://aws.random.cat"

class CatViewModel(
    private val api: CatApi = buildCatApi(BASE_URL_CAT)
) : BaseViewModel<UiStateCat>() {

    private val ERROR_URL: String = "https://http.cat/404"

    var currImage: Bitmap? = null
    var currImageUrl: String = "https://http.cat/404"

    fun getNormalCat() {
        uiState.value = UiStateCat.Loading
        viewModelScope.launch {
            val response: Response<CatImageUrl> = api.getCatPic()
            try {
                uiState.value = UiStateCat.Success(response)
            } catch (exception: Exception) {

                uiState.value =
                    UiStateCat.Error("You network don't want you to see cats :(\ntry again later")
            }
        }
    }

    sealed class UIEvent {
        data class ShowSnackbar(val message: String) : UIEvent()
    }

}