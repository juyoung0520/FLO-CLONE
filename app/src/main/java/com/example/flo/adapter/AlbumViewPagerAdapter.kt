package com.example.flo.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.flo.DetailFragment
import com.example.flo.SongFragment
import com.example.flo.VideoFragment
import com.example.flo.data.Album

class AlbumViewPagerAdapter(fragment: Fragment, album: Album) : FragmentStateAdapter(fragment) {

    private val album: Album = album

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SongFragment(album)
            1 -> DetailFragment(album)
            else -> VideoFragment()
        }
    }

}