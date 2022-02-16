package com.example.flo.view

import com.example.flo.data.Auth

interface SigninView {
    fun onSigninLoading()
    fun onSigninSuccess(auth: Auth)
    fun onSigninFailure(code: Int, message: String)
}