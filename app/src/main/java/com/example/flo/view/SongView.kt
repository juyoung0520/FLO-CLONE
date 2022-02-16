package com.example.flo.view

import com.example.flo.data.Song

interface SongView {
    fun onGetSongsLoading()
    fun onGetSongsSuccess(songs: ArrayList<Song>)
    fun onGetSongsFailure(code: Int, message: String)
}