package com.example.clone_youtube

import retrofit2.Call
import retrofit2.http.GET

class YoutubeItem(
  val id: Int,
  val title: String,
  val content: String,
  val video: String,
  val thumbnail: String
)

interface RetrofitService {
  // baseUrl 뒤 주소 call
  @GET("youtube/list/")
  fun getYoutubeItemList(): Call<Array<YoutubeItem>>
}