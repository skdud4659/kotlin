package com.example.clone_melon

import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity() {
  lateinit var melonList: Array<SongItem>
  lateinit var playPauseBtn: ImageView
  // 음악 재생
  lateinit var mediaPlayer: MediaPlayer
  // 재생 상태 체크
  var is_playing: Boolean = true
    // setter를 통해 변수와 이미지 변경을 묶어서 구현. 변수를 잘 다룸으로써 유지 보수에 좋음.
  set(value) {
    if (value == true) {
      playPauseBtn.setImageDrawable(this.resources.getDrawable(R.drawable.pause, this.theme))
    } else {
      playPauseBtn.setImageDrawable(this.resources.getDrawable(R.drawable.play, this.theme))
    }
    field = value
  }
  var position = 0
  set(value) {
    if (value <= 0) {
      field = 0
    } else if (value >= melonList.size) {
      field = melonList.size - 1
    } else {
      field = value
    }
  }
  
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_detail)
    // 페이지 진입 시 음악 재생
    melonList = intent.getSerializableExtra("list") as Array<SongItem>
    position = intent.getIntExtra("position", 0)
    Log.d("melonn", "" + position)
    playMelonItem(melonList.get(position))
    changeThumbnail(melonList.get(position))
    
    // 음악 재생 시 버튼 변경
    playPauseBtn = findViewById(R.id.play)
    playPauseBtn.setOnClickListener {
      if (is_playing == true) {
        is_playing = false
        mediaPlayer.stop()
      }
      else {
        is_playing = true
        playMelonItem(melonList.get(position))
      }
    }
    
    // 이전곡 버튼 클릭
    findViewById<ImageView>(R.id.back).setOnClickListener {
      mediaPlayer.stop()
      position = position - 1
      playMelonItem(melonList.get(position))
      changeThumbnail(melonList.get(position))
    }
    // 다음곡 버튼 클릭
    findViewById<ImageView>(R.id.next).setOnClickListener {
      Log.d("melonn", "" + position)
      mediaPlayer.stop()
      position = position + 1
      playMelonItem(melonList.get(position))
      changeThumbnail(melonList.get(position))
    }
  }
  // 음악 재생 함수
  fun playMelonItem(item: SongItem) {
    // mediaPlayer 생성.
    mediaPlayer = MediaPlayer.create(
      this,
      Uri.parse(item.song)
    )
    mediaPlayer.start()
  }
  
  // thumbnail 변경 함수
  fun changeThumbnail(item: SongItem) {
    findViewById<ImageView>(R.id.thumbnail).apply {
      var glide = Glide.with(this@DetailActivity)
      glide.load(item.thumbnail).into(this)
    }
  }
}