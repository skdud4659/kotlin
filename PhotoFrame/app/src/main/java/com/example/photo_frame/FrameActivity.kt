package com.example.photo_frame

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import java.util.*
import kotlin.concurrent.timer

class FrameActivity : AppCompatActivity() {
  private val list = mutableListOf<Uri>()
  private var currentPosition = 0
  private var timer: Timer? = null
  private val frontImage: ImageView by lazy { findViewById(R.id.frontImage) }
  private val backImage: ImageView by lazy { findViewById(R.id.backImage) }
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_frame)
  
    getPhoto()
    startTimer()
  }
  private fun getPhoto() {
    val size = intent.getIntExtra("size", 0)
    for (i in 0..size) {
      intent.getStringExtra("photo$i")?.let {
        list.add(Uri.parse(it))
      }
    }
  }
  private fun startTimer() {
    timer = timer(period = 5000) {
      runOnUiThread {
        val current = currentPosition
        val next = if (list.size <= currentPosition + 1) 0 else currentPosition + 1
        backImage.setImageURI(list[current])
        // 투명하게
        frontImage.alpha = 0f
        frontImage.setImageURI(list[next])
        frontImage.animate()
          .alpha(1.0f)
          .setDuration(1000)
          .start()
        
        currentPosition = next
      }
    }
  }
  
  override fun onStop() {
    super.onStop()
    timer?.cancel()
  }
  
  override fun onStart() {
    super.onStart()
    startTimer()
  }
  
  override fun onDestroy() {
    super.onDestroy()
    timer?.cancel()
  }
}