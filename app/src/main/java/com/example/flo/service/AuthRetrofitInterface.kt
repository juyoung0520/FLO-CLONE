package com.example.flo.service

import com.example.flo.data.AuthResponse
import com.example.flo.data.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthRetrofitInterface {
    @POST("/users")
    fun signup(@Body user: User): Call<AuthResponse>

    @POST("/users/login")
    fun signin(@Body user: User): Call<AuthResponse>

    @GET("/users/auto-login")
    fun autoSignin(@Header("X-ACCESS-TOKEN")jwt: String): Call<AuthResponse>
}