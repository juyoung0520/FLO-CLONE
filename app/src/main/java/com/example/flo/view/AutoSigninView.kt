package com.example.flo.view


interface AutoSigninView {
    fun onAutoSigninLoading()
    fun onAutoSigninSuccess()
    fun onAutoSigninFailure(code: Int, message: String)
}