package com.example.flo.data

import android.content.Context
import androidx.appcompat.app.AppCompatActivity

fun saveJwt(context: Context, jwt: String) {
    val sharedPreferences = context.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
    val editor = sharedPreferences.edit()

    editor.putString("jwt-string", jwt)
    editor.apply()
}

fun getJwt(context: Context): String {
    val sharedPreferences = context.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)

    return sharedPreferences.getString("jwt-string", "")!!
}