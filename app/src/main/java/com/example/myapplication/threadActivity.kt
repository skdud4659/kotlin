package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView

class threadActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_thread)
    
    val currentThread =Thread.currentThread()
    Log.d("testt", "" + currentThread) // Thread[main,5,main]
  
//    // 먼저 실행
//    for (a in 1..10) {
//      Log.d("testt", "" + a)
//    }
//
//    // 다음 실행
//    for (a in 1..10) {
//      Log.d("testt", "" + a)
//    }
    
//    Thread {
//      for (a in 1..1000) {
//        Log.d("testt", "A" + a)
//      }
//    }.start()
//
//    Thread {
//      for (a in 1..1000) {
//        Log.d("testt", "B" + a)
//      }
//    }.start()
    // >> for문은 코드의 순서대로 로그가 찍히지만 thread의 경우 독립적으로 진행되기 때문에 로그가 섞여서 찍힌다.
    
    Thread{
      val current = Thread.currentThread()
      Log.d("testt", "A"+currentThread) //AThread[main,5,main]
      findViewById<TextView>(R.id.text).text = "change"
      
      runOnUiThread{
        findViewById<TextView>(R.id.text).text = "change"
      }
    }.start()
    
  }
}