package com.levp.catsandmath.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.levp.catsandmath.core.buildNumFactApi
import com.levp.catsandmath.data.remote.NumFactApi
import com.levp.catsandmath.data.remote.dto.NumFact
import com.levp.catsandmath.presentation.core.CatState
import com.levp.catsandmath.presentation.core.BaseViewModel
import com.levp.catsandmath.presentation.core.UiStateNF
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import java.util.*

const val BASE_URL_NUMFACT: String = "http://numbersapi.com"


class NFViewModel(
    private val api: NumFactApi = buildNumFactApi(BASE_URL_NUMFACT)
) : BaseViewModel<UiStateNF>() {

    private val _searchQuery = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery

    private val _state = mutableStateOf(CatState())
    val state: State<CatState> = _state

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var searchJob: Job? = null

    val c = Calendar.getInstance()
    var year = c.get(Calendar.YEAR)
    var month = c.get(Calendar.MONTH)
    var day = c.get(Calendar.DAY_OF_MONTH)

    fun getFact(num: Long, fType: String, date: String) {
        uiState.value = UiStateNF.Loading

        viewModelScope.launch {
            val response: Response<NumFact> = when (fType) {
                "trivia" -> {
                    api.getByValueTrivia(num)
                }
                "math" -> {
                    api.getByValueMath(num)
                }
                "year" -> {
                    api.getByValueYear(num)
                }
                "date" -> {
                    //val txt = num_facts_ET.text.toString().split(".")
                    val txt = date.split(".")
                    api.getByDate(txt[1].toLong(), txt[0].toLong())
                }
                else -> {
                    api.getRandom()
                }
            }
            try {
                uiState.value = UiStateNF.Success(response)
            } catch (exception: Exception) {
                uiState.value = UiStateNF.Error("Network Request failed")
            }
        }
    }

    fun getRandomFact(fType: String, date: String = "$day.${month + 1}") {
        uiState.value = UiStateNF.Loading
        viewModelScope.launch {
            val response = if (fType != "date") {
                api.getRandomByType(fType)
            } else {
                val txt = date.split(".")
                api.getByDate(txt[1].toLong(), txt[0].toLong())
            }
            try {

                uiState.value = UiStateNF.Success(response)
            } catch (exception: Exception) {
                uiState.value = UiStateNF.Error("Network Request failed")
            }

        }
    }

    sealed class UIEvent {
        data class ShowSnackbar(val message: String) : UIEvent()
    }

}