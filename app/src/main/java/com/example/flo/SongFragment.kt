package com.example.flo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.flo.adapter.SideRecyclerViewAdapter
import com.example.flo.data.Album
import com.example.flo.data.Song
import com.example.flo.databinding.FragmentSongBinding
import com.example.flo.service.AlbumService
import com.example.flo.view.SongView

class SongFragment(album: Album): Fragment(), SongView {
    private lateinit var binding: FragmentSongBinding
    private lateinit var sideRecyclerViewAdapter: SideRecyclerViewAdapter
    private val album: Album = album

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSongBinding.inflate(inflater, container, false)

        binding.songMixTb.setOnClickListener {
            if (binding.songMixTb.isChecked) {
                binding.songToggleLayout.setBackgroundResource(R.drawable.btn_round_toggle_on)
                binding.songMixTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            } else {
                binding.songToggleLayout.setBackgroundResource(R.drawable.btn_round_toggle_off)
                binding.songMixTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            }
        }
        
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        initRecyclerView()
        getSongs()
    }

    private fun initRecyclerView() {
        sideRecyclerViewAdapter = SideRecyclerViewAdapter()
        binding.songSongRv.adapter = sideRecyclerViewAdapter
    }

    private fun getSongs() {
        val albumService = AlbumService()
        albumService.setSongView(this)

        albumService.getAlbumSides(album.albumIdx!!)
    }

    override fun onGetSongsLoading() {
        binding.songLoadingPb.visibility = View.VISIBLE
    }

    override fun onGetSongsSuccess(songs: ArrayList<Song>) {
        binding.songLoadingPb.visibility = View.GONE

        sideRecyclerViewAdapter.addSongs(songs)
    }

    override fun onGetSongsFailure(code: Int, message: String) {
        binding.songLoadingPb.visibility = View.GONE

        when(code) {
            400 -> Log.d("SONGFRAG/API-ERROR", message)
        }
    }
}