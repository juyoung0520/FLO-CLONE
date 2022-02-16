package com.example.flo.service

import com.example.flo.data.AlbumResponse
import com.example.flo.data.AlbumSideResponse
import com.example.flo.data.SongResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AlbumRetrofitInterface {
    @GET("/albums")
    fun getAlbums(): Call<AlbumResponse>

    @GET("/albums/{albumIdx}")
    fun getAlbumSides(@Path("albumIdx")albumIdx: Int): Call<AlbumSideResponse>
}
