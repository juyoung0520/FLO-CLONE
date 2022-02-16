package com.example.flo.service

import android.util.Log
import com.example.flo.data.AlbumResponse
import com.example.flo.data.AlbumSideResponse
import com.example.flo.view.AlbumView
import com.example.flo.view.SongView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AlbumService {
    private lateinit var albumView: AlbumView
    private lateinit var sideView: SongView

    fun setAlbumView(albumView: AlbumView) {
        this.albumView = albumView
    }

    fun setSongView(sideView: SongView) {
        this.sideView = sideView
    }

    fun getAlbums() {
        val songService = getRetrofit().create(AlbumRetrofitInterface::class.java)

        albumView.onGetAlbumsLoading()

        songService.getAlbums().enqueue(object : Callback<AlbumResponse> {
            override fun onResponse(call: Call<AlbumResponse>, response: Response<AlbumResponse>) {
                val resBody = response.body()!!

                Log.d("ALBUM/API-FLO", resBody.toString())

                when(resBody.code) {
                    1000 -> albumView.onGetAlbumsSuccess(resBody.result!!.albums)
                    else -> albumView.onGetAlbumsFailure(resBody.code, resBody.message)
                }
            }

            override fun onFailure(call: Call<AlbumResponse>, t: Throwable) {
                Log.d("ALBUM/API-ERROR", t.toString())
                albumView.onGetAlbumsFailure(400, "네트워크 오류가 발생하였습니다.")
            }

        })
    }

    fun getAlbumSides(albumIdx: Int) {
        val songService = getRetrofit().create(AlbumRetrofitInterface::class.java)

        sideView.onGetSongsLoading()

        songService.getAlbumSides(albumIdx).enqueue(object : Callback<AlbumSideResponse> {
            override fun onResponse(call: Call<AlbumSideResponse>, response: Response<AlbumSideResponse>) {
                val resBody = response.body()!!

                Log.d("SIDE/API-FLO", resBody.toString())

                when(resBody.code) {
                    1000 -> sideView.onGetSongsSuccess(resBody.result!!)
                    else -> sideView.onGetSongsFailure(resBody.code, resBody.message)
                }
            }

            override fun onFailure(call: Call<AlbumSideResponse>, t: Throwable) {
                Log.d("SIDE/API-ERROR", t.toString())
                sideView.onGetSongsFailure(400, "네트워크 오류가 발생하였습니다.")
            }
        })
    }

    fun getRetrofit() = Retrofit.Builder().baseUrl("http://13.125.121.202")
        .addConverterFactory(GsonConverterFactory.create()).build()
}