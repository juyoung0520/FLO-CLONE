package com.example.flo.view

interface SignupView {
    fun onSignupLoading()
    fun onSignupSuccess()
    fun onSignupFailure(code: Int, message: String)
}