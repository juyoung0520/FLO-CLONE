package com.example.flo.data

import com.google.gson.annotations.SerializedName

data class AlbumResult(@SerializedName("albums")val albums: ArrayList<Album>)

data class AlbumResponse(
    @SerializedName("isSuccess")val isSuccess: Boolean,
    @SerializedName("code")val code: Int,
    @SerializedName("message")val message: String,
    @SerializedName("result")val result: AlbumResult?
)

data class AlbumSideResponse(
    @SerializedName("isSuccess")val isSuccess: Boolean,
    @SerializedName("code")val code: Int,
    @SerializedName("message")val message: String,
    @SerializedName("result")val result: ArrayList<Song>
)
