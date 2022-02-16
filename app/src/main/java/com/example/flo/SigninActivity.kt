package com.example.flo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.data.Auth
import com.example.flo.data.SongDatabase
import com.example.flo.data.User
import com.example.flo.data.saveJwt
import com.example.flo.databinding.ActivitySigninBinding
import com.example.flo.service.AuthService
import com.example.flo.view.SigninView

class SigninActivity: AppCompatActivity(), SigninView{
    lateinit var binding: ActivitySigninBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signinSignupTv.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

        binding.signinLoginBtn.setOnClickListener {
            signin()
            // 왜 finish 안하고 새로 할까?
        }
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    /*private fun signin() {
        if (binding.signinEmailEt.text.toString().isEmpty() || binding.signinEmailDomainEt.text.toString().isEmpty()) {
            Toast.makeText(this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        if (binding.signinPasswordEt.text.toString().isEmpty()) {
            Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val email: String = binding.signinEmailEt.text.toString() + "@" + binding.signinEmailDomainEt.text.toString()
        val password: String = binding.signinPasswordEt.text.toString()

        val songDB = SongDatabase.getInstance(this)!!

        val user = songDB.userDao().getUser(email, password)

        // user null 아닐 때
        user?.let{
            saveJwt(user.id)
            return
        }

        Toast.makeText(this, "회원이 아닙니다.", Toast.LENGTH_SHORT).show()
    }*/

    private fun signin() {
        if (binding.signinEmailEt.text.toString().isEmpty() || binding.signinEmailDomainEt.text.toString().isEmpty()) {
            Toast.makeText(this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        if (binding.signinPasswordEt.text.toString().isEmpty()) {
            Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val email: String = binding.signinEmailEt.text.toString() + "@" + binding.signinEmailDomainEt.text.toString()
        val password: String = binding.signinPasswordEt.text.toString()
        val user = User(email, password, "")

        val authService = AuthService()
        authService.setSigninView(this)

        authService.signin(user)
    }

    private fun saveJwtInt(userIndex: Int) {
        val sharedPreferences = getSharedPreferences("auth", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putInt("jwt", userIndex)
        editor.apply()
    }

    override fun onSigninLoading() {
        binding.signinLoadingPb.visibility = View.VISIBLE
        binding.signinErrorTv.visibility = View.GONE
    }

    override fun onSigninSuccess(auth: Auth) {
        binding.signinLoadingPb.visibility = View.GONE

        // 원래 jwt String을 저장해야됨 보안적이유..
        saveJwt(this, auth.jwt)
        saveJwtInt(auth.userIdx)
        startMainActivity()
    }

    override fun onSigninFailure(code: Int, message: String) {
        binding.signinLoadingPb.visibility = View.GONE

        when(code) {
            2015, 2019, 3014 -> {
                binding.signinErrorTv.visibility = View.VISIBLE
                binding.signinErrorTv.text = message
            }
        }
    }
}