package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.adapter.SongRecyclerViewAdapter
import com.example.flo.data.Album
import com.example.flo.data.Song
import com.example.flo.data.SongDao
import com.example.flo.data.SongDatabase
import com.example.flo.databinding.FragmentListBinding
import com.google.gson.Gson

class SongListFragment: Fragment() {
    private lateinit var binding: FragmentListBinding
    private lateinit var songDB: SongDatabase
    private lateinit var songDao: SongDao

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(inflater, container, false)

        songDB = SongDatabase.getInstance(requireContext())!!
        songDao = songDB.songDao()

        return binding.root
    }

    private fun initRecyclerView() {
        val songAdapter = SongRecyclerViewAdapter(requireContext())

        songAdapter.setOnItemClickListener(object : SongRecyclerViewAdapter.OnItemClickListener {
    //            override fun onItemAlbumClick(album: Album) {
    //                changeAlbumFragment(album)
    //            }

            override fun onItemMoreClick(songId: Int) {
                songDao.updateIsLike(false, songId)

                val mainActivity = activity as MainActivity
                mainActivity.song.isLike = false
            }

        })

        binding.listPlayListRecyclerView.adapter = songAdapter
        binding.listPlayListRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        songAdapter.addSongs(songDao.getSongsByLikeState(true) as ArrayList)
    }

    private fun changeAlbumFragment(album: Album) {
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, AlbumFragment().apply {
                arguments = Bundle().apply {
                    val gson = Gson()
                    val albumJson = gson.toJson(album)
                    putString("album", albumJson)
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