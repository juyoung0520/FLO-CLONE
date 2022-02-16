package com.example.flo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.flo.adapter.AlbumViewPagerAdapter
import com.example.flo.adapter.LockerViewPagerAdapter
import com.example.flo.data.getJwt
import com.example.flo.databinding.FragmentLockerBinding
import com.example.flo.service.AuthService
import com.example.flo.view.AutoSigninView
import com.google.android.material.tabs.TabLayoutMediator


class LockerFragment : Fragment(), AutoSigninView {
    lateinit var binding: FragmentLockerBinding

    val signinTabs = arrayListOf<String>("내 리스트", "좋아요", "저장한 곡", "많이 들은", "팔로잉", "최근 감상", "iPod 음악")
    val defaultTabs = arrayListOf<String>("저장한 곡", "저장한 앨범")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLockerBinding.inflate(inflater, container, false)

        return binding.root
    }

    private fun addAdapter(tabs: ArrayList<String>, signinState: Boolean) {
        val lockerAdapter = LockerViewPagerAdapter(this, tabs.size)

        binding.lockerVp.adapter = lockerAdapter
        TabLayoutMediator(binding.lockerTl, binding.lockerVp) { tab, position ->
            tab.text = tabs[position]
            if (position == 1 && signinState) {
                tab.setIcon(R.drawable.btn_heart_selector)
            }

        }.attach()
    }

    override fun onStart() {
        super.onStart()

        initView()
    }

    private fun initView() {
        val jwt = getJwt(requireContext())

        val authService = AuthService()
        authService.setAutoSigninView(this)

        authService.autoSignin(jwt)
    }

/*    private fun initView() {
        val jwt = getJwt(requireContext())

        // 로그인 안돼있을 때
        if (jwt == "") {
            binding.lockerSinginTv.text = "로그인"

            binding.lockerSinginTv.setOnClickListener {
                startActivity(Intent(activity, SigninActivity::class.java))
            }
        } else {
            binding.lockerSinginTv.text = "로그아웃"
            binding.lockerProfileIv.visibility = View.VISIBLE
            binding.lockerProfileNameTv.visibility = View.VISIBLE

            // 자동 로그인

           binding.lockerSinginTv.setOnClickListener {
                signout()
                startActivity(Intent(activity, MainActivity::class.java))
            }
        }
    }*/

//    private fun getJwt(): Int {
//        val sharedPreference = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
//
//        return sharedPreference!!.getInt("jwt", 0)
//    }

    private fun signout() {
        val sharedPreference = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        val editor = sharedPreference!!.edit()

        editor.remove("jwt")
        editor.remove("jwt-string")
        editor.apply()
    }

    override fun onAutoSigninLoading() {
        binding.lockerLoadingPb.visibility = View.VISIBLE
    }

    override fun onAutoSigninSuccess() {
        binding.lockerLoadingPb.visibility = View.GONE

        binding.lockerSinginTv.text = "로그아웃"
        binding.lockerProfileIv.visibility = View.VISIBLE
        binding.lockerProfileNameTv.visibility = View.VISIBLE

        // 자동 로그인
        binding.lockerSinginTv.setOnClickListener {
            signout()
            startActivity(Intent(activity, MainActivity::class.java))
        }

        addAdapter(signinTabs, true)
    }

    override fun onAutoSigninFailure(code: Int, message: String) {
        binding.lockerLoadingPb.visibility = View.GONE

        when (code) {
            2002, 2001 -> {
                Log.d("AUTOSIGNIN/API-ERROR", message)

                binding.lockerSinginTv.text = "로그인"

                binding.lockerSinginTv.setOnClickListener {
                    startActivity(Intent(activity, SigninActivity::class.java))
                }

                addAdapter(defaultTabs, false)
            }
        }
    }

}