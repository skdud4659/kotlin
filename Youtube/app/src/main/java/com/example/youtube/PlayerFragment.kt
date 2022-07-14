package com.example.youtube

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.youtube.adapter.VideoAdapter
import com.example.youtube.databinding.FragmentPlayerBinding
import com.example.youtube.dto.VideoDto
import com.example.youtube.service.VideoService
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.abs

class PlayerFragment: Fragment(R.layout.fragment_player) {
  // 뷰바인딩
  private var binding: FragmentPlayerBinding? = null
  private lateinit var videoAdapter: VideoAdapter
  private var player: SimpleExoPlayer? = null
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    
    val fragmentPlayerBinding = FragmentPlayerBinding.bind(view)
    binding = fragmentPlayerBinding
    
  
    initMotionLayoutEvent(fragmentPlayerBinding)
    initRecyclerView(fragmentPlayerBinding)
    // exoPlayer 초기화
    initPlayer(fragmentPlayerBinding)
    initControlButton(fragmentPlayerBinding)
  
    getVideoList()
    
  }
  
  // motion이 움직일때 하단 navigation도 같이 motion
  private fun initMotionLayoutEvent(fragmentPlayerBinding: FragmentPlayerBinding) {
    fragmentPlayerBinding.playerMotionLayout.setTransitionListener(object: MotionLayout.TransitionListener {
      override fun onTransitionStarted(motionLayout: MotionLayout?, startId: Int, endId: Int) {}
      // 변화될때 하단도 같이
      override fun onTransitionChange(
        motionLayout: MotionLayout?,
        startId: Int,
        endId: Int,
        progress: Float
      ) {
        binding?.let {
          // 붙어있는 activity가 오게 됨.
          // also > 형변환
          (activity as MainActivity).also { mainActivity ->
            mainActivity.findViewById<MotionLayout>(R.id.mainMotionLayout).progress = abs(progress)
          }
        }
      }
    
      override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {}
      override fun onTransitionTrigger(
        motionLayout: MotionLayout?,
        triggerId: Int,
        positive: Boolean,
        progress: Float
      ) {}
    })
  }
  
  private fun initRecyclerView(fragmentPlayerBinding: FragmentPlayerBinding) {
    videoAdapter = VideoAdapter(callback = {url, title ->
      play(url, title)
    })
    fragmentPlayerBinding.fragmentRecyclerView.apply {
        adapter = videoAdapter
        layoutManager = LinearLayoutManager(context)
    }
  }
  private fun initPlayer(fragmentPlayerBinding: FragmentPlayerBinding) {
    context?.let {
      player = SimpleExoPlayer.Builder(it).build()
    }
    fragmentPlayerBinding.playerView.player = player
    binding?.let {
      player?.addListener(object : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
          super.onIsPlayingChanged(isPlaying)
//          if (isPlaying) {
//            it.bottomPlayerControlButton.setImageResource(R.drawable.pause)
//          } else {
//            it.bottomPlayerControlButton.setImageResource(R.drawable.play)
//          }
        }
      })
    }
  }
  private fun initControlButton(fragmentPlayerBinding: FragmentPlayerBinding) {
    fragmentPlayerBinding.bottomPlayerControlButton.setOnClickListener {
      val player = this.player?: return@setOnClickListener
      if (player.isPlaying) {
        player.pause()
      } else {
        player.play()
      }
    }
  
  }
  
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
  
  fun play(url: String, title: String) {
    context?.let {
      // 데이터 소스 변환 > 미디어 소스 변환
      val dataSourceFactory = DefaultDataSourceFactory(it)
      val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
        .createMediaSource(MediaItem.fromUri(Uri.parse(url)))
      player?.setMediaSource(mediaSource)
      // 데이터 가져오기 시작
      player?.prepare()
      player?.play()
    }
    binding?.let {
      it.playerMotionLayout.transitionToEnd()
      it.bottomTitleTextVIew.text = title
    }
  }
  
  override fun onStop() {
    super.onStop()
    // 앱에서 떠났을 때는 정지
    player?.pause()
  }
  
  override fun onDestroy() {
    super.onDestroy()
    // binding 해제 필수!!!!!!!
    binding = null
    // 앱을 종료했을 때는 재생
    player?.release()
  }
}