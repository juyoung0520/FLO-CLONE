package com.example.flo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.MainActivity
import com.example.flo.adapter.AlbumListRecyclerViewAdapter
import com.example.flo.adapter.SongRecyclerViewAdapter
import com.example.flo.data.Album
import com.example.flo.data.Song
import com.example.flo.data.SongDao
import com.example.flo.data.SongDatabase
import com.example.flo.databinding.FragmentListBinding
import com.google.gson.Gson

class AlbumListFragment: Fragment() {
    private lateinit var binding: FragmentListBinding
    private lateinit var songDB: SongDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(inflater, container, false)

        songDB = SongDatabase.getInstance(requireContext())!!

        return binding.root
    }

    private fun initRecyclerView() {
        val albumAdapter = AlbumListRecyclerViewAdapter()
        val jwt = getJwt()

        albumAdapter.setOnItemClickListener(object : AlbumListRecyclerViewAdapter.OnItemClickListener {
    //            override fun onItemAlbumClick(album: Album) {
    //                changeAlbumFragment(album)
    //            }

            override fun onItemMoreClick(albumId: Int) {
                songDB.albumDao().disLikeAlbum(jwt, albumId)
            }

        })

        binding.listPlayListRecyclerView.adapter = albumAdapter
        binding.listPlayListRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        albumAdapter.addAlbums(songDB.albumDao().getLikedAlbums(jwt) as ArrayList)
    }

    private fun getJwt(): Int {
        val sharedPreference = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)

        return sharedPreference!!.getInt("jwt", 0)
    }

    private fun changeAlbumFragment(album: Album) {
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, AlbumFragment().apply {
                arguments = Bundle().apply {
                    val gson = Gson()
                    val albumJson = gson.toJson(album)
                    putString("album", albumJson)
                    // 나중에 고치기..
                    putString("fragment", "list")
                }
            })
            .commitAllowingStateLoss()
    }

    override fun onStart() {
        super.onStart()

        initRecyclerView()
    }
}