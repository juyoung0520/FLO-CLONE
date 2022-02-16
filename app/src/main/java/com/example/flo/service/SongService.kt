package com.example.flo.service

import android.util.Log
import com.example.flo.data.AuthResponse
import com.example.flo.data.SongResponse
import com.example.flo.view.SigninView
import com.example.flo.view.SignupView
import com.example.flo.view.SongView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SongService() {
    private lateinit var songView: SongView

    fun setSongView(songView: SongView) {
        this.songView = songView
    }

    fun getSongs() {
        val songService = getRetrofit().create(SongRetrofitInterface::class.java)

        songView.onGetSongsLoading()

        songService.getSongs().enqueue(object : Callback<SongResponse> {
            override fun onResponse(call: Call<SongResponse>, response: Response<SongResponse>) {
                val resBody = response.body()!!

                Log.d("SONG/API-FLO", resBody.toString())

                when(resBody.code) {
                    1000 -> songView.onGetSongsSuccess(resBody.result!!.songs)
                    else -> songView.onGetSongsFailure(resBody.code, resBody.message)
                }
            }

            override fun onFailure(call: Call<SongResponse>, t: Throwable) {
                Log.d("SONG/API-ERROR", t.toString())
                songView.onGetSongsFailure(400, "네트워크 오류가 발생하였습니다.")
            }

        })
    }

    fun getRetrofit() = Retrofit.Builder().baseUrl("http://13.125.121.202")
        .addConverterFactory(GsonConverterFactory.create()).build()
}