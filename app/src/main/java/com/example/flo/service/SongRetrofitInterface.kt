package com.example.flo.service

import com.example.flo.data.SongResponse
import retrofit2.Call
import retrofit2.http.GET

interface SongRetrofitInterface {
    @GET("/songs")
    fun getSongs(): Call<SongResponse>
}