package com.example.flo.service

import android.util.Log
import com.example.flo.data.AuthResponse
import com.example.flo.data.User
import com.example.flo.view.SigninView
import com.example.flo.view.SignupView
import com.example.flo.view.AutoSigninView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthService {
    private lateinit var signupView: SignupView
    private lateinit var signinView: SigninView
    private lateinit var autoSigninView: AutoSigninView

    fun setSignupView(signupView: SignupView) {
        this.signupView = signupView
    }

    fun setSigninView(signinView: SigninView) {
        this.signinView = signinView
    }

    fun setAutoSigninView(autoSigninView: AutoSigninView) {
        this.autoSigninView = autoSigninView
    }


    fun signup(user: User) {
        val authService = getRetrofit().create(AuthRetrofitInterface::class.java)

        signupView.onSignupLoading()

        authService.signup(user).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                val resBody = response.body()!!

                Log.d("SIGNUPPACT/API-FLO", resBody.toString())

                when(resBody.code) {
                    1000 -> signupView.onSignupSuccess()
                    else -> signupView.onSignupFailure(resBody.code, resBody.message)
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Log.d("SIGNUPPACT/API-ERROR", t.toString())
                signupView.onSignupFailure(400, "네트워크 오류가 발생하였습니다.")
            }

        })
    }

    fun signin(user: User) {
        val authService = getRetrofit().create(AuthRetrofitInterface::class.java)

        signinView.onSigninLoading()

        authService.signin(user).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                val resBody = response.body()!!

                Log.d("SIGNIN/API-FLO", resBody.toString())

                when(resBody.code) {
                    1000 -> signinView.onSigninSuccess(resBody.result!!)
                    else -> signinView.onSigninFailure(resBody.code, resBody.message)
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Log.d("SIGNIN/API-ERROR", t.toString())
                signinView.onSigninFailure(400, "네트워크 오류가 발생하였습니다.")
            }

        })
    }

    fun autoSignin(jwt: String) {
        val authService = getRetrofit().create(AuthRetrofitInterface::class.java)

        autoSigninView.onAutoSigninLoading()

        authService.autoSignin(jwt).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                val resBody = response.body()!!

                Log.d("AUTOSIGNIN/API-FLO", resBody.toString())

                when(resBody.code) {
                    1000 -> autoSigninView.onAutoSigninSuccess()
                    else -> autoSigninView.onAutoSigninFailure(resBody.code, resBody.message)
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Log.d("AUTOSIGNIN/API-ERROR", t.toString())
                autoSigninView.onAutoSigninFailure(400, "네트워크 오류가 발생하였습니다.")
            }

        })
    }

    fun getRetrofit() = Retrofit.Builder().baseUrl("http://13.125.121.202")
            .addConverterFactory(GsonConverterFactory.create()).build()


}