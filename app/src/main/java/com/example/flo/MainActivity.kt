package com.example.flo

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.data.Album
import com.example.flo.data.Song
import com.example.flo.data.SongDao
import com.example.flo.data.SongDatabase
import com.example.flo.databinding.ActivityMainBinding
import com.google.gson.Gson


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var activityResultLauncher : ActivityResultLauncher<Intent>
    private val gson: Gson = Gson()

    private lateinit var player : Player

    private var mediaPlayer: MediaPlayer? = null
    private var MUSIC_MAX: Int = 0

    // DB
    var song: Song = Song()
    private lateinit var songDB: SongDatabase
    private lateinit var songDAO: SongDao

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_BottomNaviTemplate)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initNavigation()

        initBinding()

        initActivivtyResult()

        // DB
        initSongDB()

        initAlbumDB()
    }

    private fun initNavigation() {
        supportFragmentManager.beginTransaction().replace(R.id.main_frm, HomeFragment())
            .commitAllowingStateLoss()
    }

    private fun initBinding() {
        binding.mainPlayerLayout.setOnClickListener {
            val intent = Intent(this, SongActivity::class.java)

            val json = gson.toJson(song)
            intent.putExtra("song", json)

            activityResultLauncher.launch(intent)
        }

        binding.mainMiniplayerBtn.setOnClickListener {
            setPlayerStatus(true)
            song.isPlaying = true
        }

        binding.mainPauseBtn.setOnClickListener {
            setPlayerStatus(false)
            song.isPlaying = false
        }

        binding.mainBnv.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, HomeFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.lookFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LookFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.searchFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, SearchFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.lockerFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LockerFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

            }
            false
        }
    }

    private fun initActivivtyResult() {
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK) {
                    val songJson = it.data?.getStringExtra("song")

                    if (songJson != null) {
                        song = gson.fromJson(songJson, Song::class.java)

                        setMiniPlayer(song)
                    }
                }
            }
    }

    private fun setMiniPlayer(song: Song) {
        binding.mainTitleTv.text = song.title
        binding.mainSingerTv.text = song.singer
        binding.mainPlayBarSb.progress = song.currentTime * 1000 / song.songLength

        setPlayerStatus(song.isPlaying)
    }

    private fun setPlayerStatus(isPlaying: Boolean) {
        if(isPlaying) {
            binding.mainMiniplayerBtn.visibility = View.GONE
            binding.mainPauseBtn.visibility = View.VISIBLE
        } else {
            binding.mainMiniplayerBtn.visibility = View.VISIBLE
            binding.mainPauseBtn.visibility = View.GONE
        }
    }

    override fun onStart() {
        super.onStart()

        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val songId = sharedPreferences.getInt("songId", 0)

        song = if (songId == 0) {
            // id 1?????? ??????
            songDAO.getSong(1)
        } else {
            songDAO.getSong(songId)
        }

        setMiniPlayer(song)
    }

    override fun onDestroy() {
        super.onDestroy()
        // sharedPreference??? ??????
        val editor = getSharedPreferences("song", MODE_PRIVATE).edit()

        editor.putInt("songId", song.id)
        editor.apply()
    }

    // DB
    private fun initSongDB() {
        songDB = SongDatabase.getInstance(this)!!
        songDAO = songDB.songDao()

        val songs = songDAO.getSongs()

        if (songs.isNotEmpty()) {
            return
        }

        // DB ????????? ???, ?????????
        songDAO.apply {
            insert(Song("strawberry moon", "????????? (IU)", 205, false, 0, "lilac", R.drawable.img_album_exp3, ""))
            insert(Song("Savage", "aespa", 238, false, 0, "butter", R.drawable.img_album_exp4, ""))
            insert(Song("Lovesick Girls", "BLANCKPINK", 192, false, 0, "lilac", R.drawable.img_album_exp5, ""))
            insert(Song("butter", "???????????????", 164, false, 0, "butter", R.drawable.img_album_exp1, ""))
            insert(Song("?????????", "????????? (IU)", 214, false, 0, "lilac", R.drawable.img_album_exp2, ""))
            insert(Song("Pretty Savage", "BLANCKPINK", 199, false, 0, "butter", R.drawable.img_album_exp5, ""))
        }
    }

    private fun initAlbumDB() {
        val albums = songDB.albumDao().getAlbums()

        if (albums.isNotEmpty()) {
            return
        }

        songDB.albumDao().apply {
                insert(
                    Album("IU 5th Album 'LILAC'", "????????? (IU)", "???????????????????????????", "EDAM??????????????????",
                    "'????????? (IU)' ?????? 5??? [LILAC]", "2021.03.25", "??????", "?????? ???", R.drawable.img_album_exp2, "")
                )
                insert(
                    Album("Butter", "???????????????", "YG PLUS", "BIGHIT MUSIC",
                    "?????????????????? ?????????, ??? ????????? ?????? <Butter>", "2021.05.21", "??????", "??????", R.drawable.img_album_exp1, "")
                )
                insert(
                    Album("strawberry moon", "????????? (IU)", "???????????????????????????", "EDAM??????????????????",
                    "'????????? (IU)' Digital Single [strawberry moon]", "2021.10.19", "??????", "???", R.drawable.img_album_exp3, "")
                )
                insert(
                    Album("Savage", "aespa", "Dreamus", "SM ENTERTAINMENT",
                    "Album Introduce\n'???????????? ?????????' aespa, ??? ???????????? 'Savage' ??????!", "2021.10.05", "??????", "??????", R.drawable.img_album_exp4, "")
                )
                insert(
                    Album("THE ALBUM", "BLACKPINK", "YG PLUS", "YG Entertainment",
                    "2016??? 'SQUARE ONE'?????? ???????????? ????????? ??????????????? ????????? ??????????????? ??? ???????????? 'The Album'??? 10??? 2??? ????????????.", "2020.10.02", "??????", "??????, ??????", R.drawable.img_album_exp5, "")
                )
        }
    }

    inner class Player(private val songLength: Int, var currentTime: Int, var isPlaying: Boolean): Thread() {
        override fun run() {
            try {
                while (true) {
                    if (currentTime >= songLength) {
                        break
                    }

                    if (isPlaying) {
                        sleep(1000)
                        currentTime++

                        // runOnUiThread ???????????? ???.
                        runOnUiThread {
                            binding.mainPlayBarSb.progress = currentTime * 1000 / songLength
                        }
                    }
                }
            } catch (e: InterruptedException) {
                Log.d("interrupt","???????????? ?????????????????????.")
            }
        }
    }

}

