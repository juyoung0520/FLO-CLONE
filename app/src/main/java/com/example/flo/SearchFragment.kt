package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.flo.adapter.AlbumViewPagerAdapter
import com.example.flo.databinding.FragmentSearchBinding
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayoutMediator


class SearchFragment : Fragment() {
    lateinit var binding: FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }


}