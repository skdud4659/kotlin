package com.example.clone_youtube

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.MediaController
import android.widget.VideoView

class YoutubePlayer : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_youtube_player)
    
    // 데이터 받기
    val videoUrl = intent.getStringExtra("video_url")
    // 영상 재생 > mediaController
    // ExoPlayer > ott에서 주로 씀(기능이 다양하고 사용이 용이함)
    val mediaController = MediaController(this)
    findViewById<VideoView>(R.id.video_view).apply {
      this.setVideoPath(videoUrl)
      this.requestFocus()
      this.start()
      mediaController.show()
    }
  }
}