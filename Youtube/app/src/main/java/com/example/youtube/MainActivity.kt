package com.example.youtube

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.youtube.adapter.VideoAdapter
import com.example.youtube.dto.VideoDto
import com.example.youtube.service.VideoService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
  
  private lateinit var videoAdapter: VideoAdapter
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    
    // frameLayout(fragmentContainer)에 fragment 붙이기
    supportFragmentManager.beginTransaction()
      .replace(R.id.fragmentContainer, PlayerFragment())
      .commit()
  
    getVideoList()
    
    // videoAdapter 연결
    videoAdapter = VideoAdapter(callback = {url, title ->
      // fragment의 함수 가져오기.
      supportFragmentManager.fragments.find { it is PlayerFragment }?.let {
        (it as PlayerFragment).play(url, title)
      }
    })
    findViewById<RecyclerView>(R.id.mainRecyclerView).apply {
      adapter = videoAdapter
      layoutManager = LinearLayoutManager(context)
    }
  }
  
  // API에서 video 리스트 받아오기
  private fun getVideoList() {
    // instance
    val retrofit = Retrofit.Builder()
      .baseUrl("https://run.mocky.io/")
      .addConverterFactory(GsonConverterFactory.create())
      .build()
  
    // 통신
    retrofit.create(VideoService::class.java).also {
      it.listVideos()
        .enqueue(object: Callback<VideoDto> {
          override fun onResponse(call: Call<VideoDto>, response: Response<VideoDto>) {
            if (response.isSuccessful.not()) {
              Log.d("MainActivity", "res fail")
              return
            }
            response.body()?.let {videoDto ->
              videoAdapter.submitList(videoDto.videos)
            }
          }
  
          override fun onFailure(call: Call<VideoDto>, t: Throwable) {
            TODO("Not yet implemented")
          }
        })
    }
  }
}