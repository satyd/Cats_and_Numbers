package com.levp.catsandmath.data.remote

import com.levp.catsandmath.data.remote.dto.NumFact
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

import retrofit2.http.Query

interface NumFactApi {
    @GET("/api/random")
    suspend fun getQuestions(@Query("count") count: Int): Response<List<NumFact>>

    @GET("/{num}?json")
    suspend fun getByValue(@Path("num") num : Long): Response<NumFact>

    @GET("/{month}/{day}/date?json")
    suspend fun getByDate(@Path("month") month : Long, @Path("day") day : Long,): Response<NumFact>

    @GET("/{num}/math?json")
    suspend fun getByValueMath(@Path("num") num : Long): Response<NumFact>

    @GET("/{num}/trivia?json")
    suspend fun getByValueTrivia(@Path("num") num : Long): Response<NumFact>

    @GET("/{num}/year?json")
    suspend fun getByValueYear(@Path("num") num : Long): Response<NumFact>

    @GET("/random?json")
    suspend fun getRandom(): Response<NumFact>

    @GET("/random/{type}?json")
    suspend fun getRandomByType(@Path("type") type:String): Response<NumFact>

    @GET("/random")
    suspend fun getRandomInRange(@Query("min") min:Int, @Query("max") max:Int): Response<NumFact>
    //
}