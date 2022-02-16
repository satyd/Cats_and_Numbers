package com.levp.catsandmath.data.remote

import com.levp.catsandmath.data.remote.dto.CatImageUrl
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CatApi {

    @GET("/meow")
    suspend fun getCatPic() : Response<CatImageUrl>

    @GET("/{error}")
    suspend fun getCatError(@Path("error") error : Int) : CatImageUrl

    companion object {
        const val BASE_URL = "https://aws.random.cat"
        const val BASE_URL_2 = "https://docs.thecatapi.com/"
        const val BASE_URL_ERROR = "https://http.cat/"
    }
}