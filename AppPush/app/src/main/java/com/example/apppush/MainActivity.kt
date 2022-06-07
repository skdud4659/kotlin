package com.example.apppush

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView

class MainActivity : AppCompatActivity() {
  private val result: TextView by lazy { findViewById(R.id.result) }
  private val firebaseToken: TextView by lazy { findViewById(R.id.token) }
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
  
    initFirebase()
  }
  
  private fun initFirebase() {
    com.google.firebase.messaging.FirebaseMessaging.getInstance().token
      .addOnCompleteListener { task ->
        if (task.isSuccessful) {
          firebaseToken.text = task.result
        }
      }
  }
}