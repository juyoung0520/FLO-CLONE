package com.example.flo

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.data.AuthResponse
import com.example.flo.data.User
import com.example.flo.databinding.ActivitySignupBinding
import com.example.flo.service.AuthRetrofitInterface
import com.example.flo.service.AuthService
import com.example.flo.view.SignupView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SignupActivity: AppCompatActivity(), SignupView {
    lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signupSignupBtn.setOnClickListener {
            signup()
        }
    }

    private fun getInput(): User {
        val email: String = binding.signupEmailEt.text.toString() + "@" + binding.signupEmailDomainEt.text.toString()
        val password: String = binding.signupPasswordEt.text.toString()
        val name: String = binding.signupNameEt.text.toString()

        return User(email, password, name)
    }

/*    private fun signup() {
        if (binding.signupEmailEt.text.toString().isEmpty() || binding.signupEmailDomainEt.text.toString().isEmpty()) {
            Toast.makeText(this, "이메일 형식이 잘못 되었습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        if (binding.signupNameEt.text.toString().isEmpty()) {
            Toast.makeText(this, "이름 형식이 잘못 되었습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        if (binding.signupPasswordEt.text.toString().isEmpty() || binding.signupPasswordCheckEt.text.toString().isEmpty()) {
            Toast.makeText(this, "비밀번호 형식이 잘못 되었습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        if (binding.signupPasswordEt.text.toString() != binding.signupPasswordCheckEt.text.toString()) {
            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
        }


        val userDB = SongDatabase.getInstance(this)!!
        userDB.userDao().insert(getInput())
    }*/

    private fun signup() {
        if (binding.signupEmailEt.text.toString().isEmpty() || binding.signupEmailDomainEt.text.toString().isEmpty()) {
            Toast.makeText(this, "이메일 형식이 잘못 되었습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        if (binding.signupNameEt.text.toString().isEmpty()) {
            Toast.makeText(this, "이름 형식이 잘못 되었습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        if (binding.signupPasswordEt.text.toString().isEmpty() || binding.signupPasswordCheckEt.text.toString().isEmpty()) {
            Toast.makeText(this, "비밀번호 형식이 잘못 되었습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        if (binding.signupPasswordEt.text.toString() != binding.signupPasswordCheckEt.text.toString()) {
            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
        }

        val authService = AuthService()
        authService.setSignupView(this)

        authService.signup(getInput())
    }

    override fun onSignupLoading() {
        binding.signupLoadingPb.visibility = View.VISIBLE
        binding.signupErrorEmailTv.visibility = View.GONE
    }

    override fun onSignupSuccess() {
        binding.signupLoadingPb.visibility = View.GONE
        binding.signupErrorEmailTv.visibility = View.GONE

        finish()
    }

    override fun onSignupFailure(code: Int, message: String) {
        binding.signupLoadingPb.visibility = View.GONE
        binding.signupErrorEmailTv.visibility = View.VISIBLE

        when (code) {
            2016, 2017 -> {
                binding.signupErrorEmailTv.visibility = View.VISIBLE
                binding.signupErrorEmailTv.text = message
            }
        }
    }

}