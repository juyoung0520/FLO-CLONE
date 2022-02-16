package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.data.Album
import com.example.flo.databinding.FragmentBannerBinding
import com.example.flo.databinding.FragmentDetailBinding

class DetailFragment(album: Album): Fragment() {
    lateinit var binding: FragmentDetailBinding
    private val album: Album = album

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)

        binding.detailAlbumNameStrTv.text = album.name
        binding.detailAlbumArtistStrTv.text = album.artist
        binding.detailAlbumPublisherStrTv.text = album.publisher
        binding.detailAlbumAgencyStrTv.text = album.agency
        binding.detailAlbumDiscriptionStrTv.text = album.discription
        return binding.root
    }
}