package com.example.youtube.service

import com.example.youtube.dto.VideoDto
import retrofit2.Call
import retrofit2.http.GET

interface VideoService {
  @GET("v3/3405abdf-f5ba-44a7-8937-3d06e875589f")
  fun listVideos(): Call<VideoDto>
}