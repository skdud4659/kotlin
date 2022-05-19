package com.example.clone_instagram

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class SplashActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_splash)
    
    val sharedPreferences = getSharedPreferences("user_token", Context.MODE_PRIVATE)
    val token = sharedPreferences.getString("token", "empty")
    when (token) {
      "empty" -> { startActivity(Intent(this, MainActivity::class.java))}
      else -> { startActivity(Intent(this, BoardActivity::class.java))}
    }
  }
}