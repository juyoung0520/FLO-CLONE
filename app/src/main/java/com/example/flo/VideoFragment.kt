package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentBannerBinding
import com.example.flo.databinding.FragmentDetailBinding
import com.example.flo.databinding.FragmentVideoBinding

class VideoFragment: Fragment() {
    lateinit var binding: FragmentVideoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVideoBinding.inflate(inflater, container, false)

        binding.videoMvExp1Iv.clipToOutline = true
        binding.videoMvExp2Iv.clipToOutline = true
        binding.videoMvExp3Iv.clipToOutline = true

        return binding.root
    }
}