package com.example.flo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat.getColor
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.flo.adapter.AlbumViewPagerAdapter
import com.example.flo.data.Album
import com.example.flo.data.Like
import com.example.flo.data.SongDatabase
import com.example.flo.databinding.FragmentAlbumBinding
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson

class AlbumFragment : Fragment() {
    lateinit var binding: FragmentAlbumBinding
    private val gson: Gson = Gson()

    private var isLike: Boolean = false

    val information = arrayListOf("수록곡", "상세정보", "영상")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumBinding.inflate(inflater, container, false)

        val albumJson = arguments?.getString("album")
        val album = gson.fromJson(albumJson, Album::class.java)
        val origin = arguments?.getString("fragment")

        isLike = isLikeAlbum(album.id)

        initAlbum(album)

        val albumAdapter = AlbumViewPagerAdapter(this, album)

        binding.albumContentVp.adapter = albumAdapter
        TabLayoutMediator(binding.albumTabLayout, binding.albumContentVp) {
            tab, position -> tab.text = information[position]
        }.attach()

        binding.albumToolbar.setNavigationOnClickListener {
            if (origin.equals("home")) {
                (context as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frm, HomeFragment())
                    .commitAllowingStateLoss()
            } else if (origin.equals("list")) {
                (context as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frm, LockerFragment())
                    .commitAllowingStateLoss()
            }
        }

        return binding.root
    }

    private fun initAlbum(album: Album) {
        if (album.coverImg == null) {
            Glide.with(requireContext()).load(album.coverImgUrl).into(binding.albumAlbumIb)
        } else {
            binding.albumAlbumIb.setImageResource(album.coverImg!!)
        }

        binding.albumAlbumTitleTv.text = album.name
        binding.albumAlbumSignerTv.text = album.artist
        binding.albumAlbumInfoTv.text =
            album.releaseDate + " | " + album.type + " | " + album.category

        binding.albumAlbumIb.clipToOutline = true

        if (isLike) {
            binding.albumToolbar.menu.findItem(R.id.toolbar_heart).setIcon(R.drawable.ic_my_like_on)
        } else {
            binding.albumToolbar.menu.findItem(R.id.toolbar_heart).setIcon(R.drawable.ic_my_like_off)
        }

        val userId = getJwt()

        binding.albumToolbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.toolbar_heart -> {
                    if (isLike) {
                        it.setIcon(R.drawable.ic_my_like_off)
                        dislikeAlbum(userId, album.id)
                        isLike = false
                    } else {
                        it.setIcon(R.drawable.ic_my_like_on)
                        likeAlbum(userId, album.id)
                        isLike = true
                    }
                    true
                }
                else -> false
            }
        }
    }

    private fun getJwt(): Int {
        val sharedPreference = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)

        return sharedPreference!!.getInt("jwt", 0)
    }

    private fun likeAlbum(userId: Int, albumId: Int) {
        val songDB = SongDatabase.getInstance(requireContext())!!

        val like = Like(userId, albumId)

        songDB.albumDao().likeAlbum(like)
    }

    private fun dislikeAlbum(userId: Int, albumId: Int) {
        val songDatabase = SongDatabase.getInstance(requireContext())!!

        songDatabase.albumDao().disLikeAlbum(userId, albumId)
    }

    private fun isLikeAlbum(albumId: Int): Boolean {
        val songDB = SongDatabase.getInstance(requireContext())!!
        val userId = getJwt()

        val likedId = songDB.albumDao().isLikeAlbum(userId, albumId)

        return likedId != null
    }

}