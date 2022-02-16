package com.example.flo

import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.data.Song
import com.example.flo.data.SongDao
import com.example.flo.data.SongDatabase
import com.example.flo.databinding.ActivitySongBinding
import com.google.gson.Gson

class SongActivity : AppCompatActivity() {
    lateinit var binding: ActivitySongBinding
    private var gson: Gson = Gson()

    private lateinit var player: Player
    private val handler = Handler(Looper.getMainLooper())

    private var mediaPlayer: MediaPlayer? = null
    private var MUSIC_MAX: Int = 0

    // DB
    private lateinit var songDB: SongDatabase
    private lateinit var songDao: SongDao
    private var songs = ArrayList<Song>()
    private var pos = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSongs()

        initPos()

        setPlayer(songs[pos])

        initBinding()
    }

    private fun initSongs() {
        songDB = SongDatabase.getInstance(this)!!
        songDao = songDB.songDao()

        songs.addAll(songDao.getSongs())
    }

    private fun initPos() {
        if (!intent.hasExtra("song")) {
            return
        }

        val songJson = intent.getStringExtra("song")
        val song = gson.fromJson(songJson, Song::class.java)

        for (i in 0 until songs.size) {
            if (songs[i].id == song.id) {
                pos = i
                break
            }
        }

        songs[pos] = song
    }

    private fun setPlayer(song: Song) {
        player = Player(song.songLength, song.currentTime, song.isPlaying)
        player.start()

        setSongBinding(song)

        setMusic(song)
    }

    private fun setSongBinding(song: Song) {
        binding.songAlbumTitleTv.text = song.title
        binding.songAlbumSingerTv.text = song.singer
        binding.songAlbumIb.setImageResource(song.albumImg)
        binding.songMusicEndTv.text = String.format("%02d:%02d", song.songLength / 60, song.songLength % 60)

        binding.songPlayBarSb.progress = song.currentTime * 1000 / song.songLength
        binding.songMusicStartTv.text = String.format("%02d:%02d", song.currentTime / 60, song.currentTime % 60)

        setPlayerStatus(song.isPlaying)
        setLike(song.isLike)
    }

    private fun setMusic(song: Song) {
        val musicFile = resources.getIdentifier(song.music, "raw", this.packageName)
        mediaPlayer = MediaPlayer.create(this, musicFile)

        MUSIC_MAX = mediaPlayer?.duration!!

        val mediaTime = binding.songPlayBarSb.progress * MUSIC_MAX / 1000
        mediaPlayer?.seekTo(mediaTime)

        if (song.isPlaying) {
            mediaPlayer?.start()
        }
    }

    private fun setLike(isLike: Boolean) {
        songs[pos].isLike = isLike
        songDao.updateIsLike(isLike, songs[pos].id)

        if (isLike) {
            binding.songHeartIb.setImageResource(R.drawable.ic_my_like_on)
        } else {
            binding.songHeartIb.setImageResource(R.drawable.ic_my_like_off)
        }
    }

    private fun initBinding() {
        binding.songAlbumIb.clipToOutline = true

        binding.songDownIb.setOnClickListener {
            val song = songs[pos]
            song.currentTime = binding.songPlayBarSb.progress * song.songLength / 1000

            val intent = Intent(this, MainActivity::class.java).apply {
                // gson 으로 변경
                val songJson = gson.toJson(song)
                putExtra("song", songJson)
            }

            setResult(RESULT_OK, intent)
            finish()
        }

        binding.songPlayerPlayIb.setOnClickListener {
            setPlayerStatus(true)
            player.isPlaying = true
            songs[pos].isPlaying = true
            mediaPlayer?.start()
        }

        binding.songPlayerPauseIb.setOnClickListener {
            stopMusic(songs[pos])
            songs[pos].currentTime = binding.songPlayBarSb.progress * songs[pos].songLength / 1000
        }

        binding.songRepeatOffIb.setOnClickListener {
            setRepeatStatus(1)
        }

        binding.songRepeatOnIb.setOnClickListener {
            setRepeatStatus(2)
        }

        binding.songRepeatOnOneIb.setOnClickListener {
            setRepeatStatus(3)
        }

        binding.songPlayerPreviousIb.setOnClickListener {
            moveSong(-1)
        }

        binding.songPlayerNextIb.setOnClickListener {
            moveSong(+1)
        }

        binding.songHeartIb.setOnClickListener {
            Log.d("prvLike", songs[pos].isLike.toString())
            setLike(songs[pos].isLike.not())
        }

        binding.songPlayBarSb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                player.currentTime = p1 * songs[pos].songLength / 1000
                songs[pos].currentTime = player.currentTime
                binding.songMusicStartTv.text =
                    String.format("%02d:%02d", player.currentTime / 60, player.currentTime % 60)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                mediaPlayer?.pause()
                player.isPlaying = false
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                val progress = p0!!.progress
                val mediaTime = progress * MUSIC_MAX / 1000

                mediaPlayer?.seekTo(mediaTime)
                player.isPlaying = songs[pos].isPlaying

                if (player.isPlaying) {
                    mediaPlayer?.start()
                }
            }
        })
    }

    private fun setPlayerStatus(isPlaying: Boolean) {
        if(isPlaying) {
            binding.songPlayerPlayIb.visibility = View.GONE
            binding.songPlayerPauseIb.visibility = View.VISIBLE
        } else {
            binding.songPlayerPlayIb.visibility = View.VISIBLE
            binding.songPlayerPauseIb.visibility = View.GONE
        }
    }

    private fun setRepeatStatus(num: Int) {
        when (num) {
            1 -> {
                binding.songRepeatOnIb.visibility = View.VISIBLE
                binding.songRepeatOnOneIb.visibility = View.GONE
                binding.songRepeatOffIb.visibility = View.GONE
            }
            2 -> {
                binding.songRepeatOnOneIb.visibility = View.VISIBLE
                binding.songRepeatOnIb.visibility = View.GONE
                binding.songRepeatOffIb.visibility = View.GONE
            }
            else -> {
                binding.songRepeatOffIb.visibility = View.VISIBLE
                binding.songRepeatOnOneIb.visibility = View.GONE
                binding.songRepeatOnIb.visibility = View.GONE
            }
        }
    }

    private fun moveSong(direct: Int) {
        songs[pos].currentTime = 0
        val isPlaying = songs[pos].isPlaying

        if (pos + direct < 0) {
            Toast.makeText(this, "first song", Toast.LENGTH_SHORT).show()
            return
        }

        if (pos + direct >= songs.size) {
            Toast.makeText(this, "last song", Toast.LENGTH_SHORT).show()
            stopMusic(songs[pos])
            return
        }

        songs[pos].isPlaying = false

        pos += direct
        songs[pos].isPlaying = isPlaying

        player.interrupt()
        mediaPlayer?.release()
        mediaPlayer = null

        setPlayer(songs[pos])
    }

    private fun stopMusic(song: Song) {
        setPlayerStatus(false)
        mediaPlayer?.pause()
        player.isPlaying = false
        song.isPlaying = false
    }

    override fun onPause() {
        super.onPause()

        stopMusic(songs[pos])

        // 간단한 설정 값 저장
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        // 객체 -> json
        editor.putInt("songId", songs[pos].id)

        editor.apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        player.interrupt()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    inner class Player(private val songLength: Int, var currentTime: Int, var isPlaying: Boolean): Thread() {
        override fun run() {
            try {
                while (true) {
                    if (currentTime >= songLength) {
                        handler.post{
                            moveSong(+1)
                        }
                        break
                    }

                    if (isPlaying) {
                        sleep(1000)
                        currentTime++

                        // runOnUiThread 사용해도 됨.
                        handler.post {
                            binding.songPlayBarSb.progress = currentTime * 1000 / songLength
                            binding.songMusicStartTv.text =
                                String.format("%02d:%02d", currentTime / 60, currentTime % 60)
                        }
                    }
                }
            } catch (e: InterruptedException) {
                Log.d("interrupt","쓰레드가 종료되었습니다.")
            }
        }
    }

}