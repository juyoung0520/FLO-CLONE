package com.example.flo.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.flo.AlbumListFragment
import com.example.flo.SongListFragment

class LockerViewPagerAdapter(fragment: Fragment, pages: Int): FragmentStateAdapter(fragment) {
    private var pages: Int = 0

    init {
        this.pages = pages
    }

    override fun getItemCount(): Int {
        return pages
    }

    override fun createFragment(position: Int): Fragment {
        if (itemCount == 2) {
            return when(position) {
                0 -> SongListFragment()
                1 -> AlbumListFragment()
                else -> Fragment()
            }
        }

        return when(position) {
            0 -> SongListFragment()
            1 -> AlbumListFragment()
            else -> Fragment()
        }
    }
}