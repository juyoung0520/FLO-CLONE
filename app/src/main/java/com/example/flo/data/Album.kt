package com.example.flo.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "AlbumTable")
data class Album(
    @SerializedName("title") val name: String = "",
    @SerializedName("singer") val artist: String = "",
    val publisher: String = "",
    val agency: String = "",
    val discription: String = "",
    val releaseDate: String = "",
    val type: String = "",
    val category: String = "",
    val coverImg: Int? = null,
    val coverImgUrl: String? = "",
    val albumIdx: Int? = null,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
