package com.example.apppush

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {
  private val result: TextView by lazy { findViewById(R.id.result) }
  private val firebaseToken: TextView by lazy { findViewById(R.id.token) }
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
  
    initFirebase()
    updateResult()
  }
  
  // 앱이 열린 상태에서 재실행
  override fun onNewIntent(intent: Intent?) {
    super.onNewIntent(intent)
    setIntent(intent)
    updateResult(true)
  }
  
  private fun initFirebase() {
    FirebaseMessaging.getInstance().token
      .addOnCompleteListener { task ->
        if (task.isSuccessful) {
          val result = task.result
          firebaseToken.text = result
          Log.d("token", result)
        }
      }
  }
  
  // isNewIntent : 앱을 새로 열었는지 연 상태에서 알림을 눌렀는지 구분
  @SuppressLint("SetTextI18n")
  private fun updateResult(isNewIntent: Boolean = false) {
    result.text = (intent.getStringExtra("notificationType") ?: "앱 런처") + if (isNewIntent) {
      "(으)로 갱신했습니다."
    } else {
      "(으)로 실행했습니다."
    }
  }
}