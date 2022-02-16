package com.example.flo.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SongTable")
data class Song(
    var title: String = "",
    var singer: String = "",
    var songLength: Int = 0,
    var isPlaying: Boolean = false,
    var currentTime: Int = 0,
    var music: String = "",
    // album id 추가
    var albumImg: Int = 0,
    var coverImgUrl: String? = null,
    var isLike: Boolean = false,
    val albumIdx: Int? = null,
    val songIdx: Int? = null,
) {
    // 변수에 안넣고 이렇게 하면 이미 생성해 놓은 코드에 오류 안남
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}