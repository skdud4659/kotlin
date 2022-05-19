package com.example.clone_melon

import retrofit2.http.GET
import retrofit2.Call
import java.io.Serializable

class SongItem(
  val id: Int,
  val title: String,
  val song: String,
  val thumbnail: String
  // Serializable > 객체를 자동 전환하여 데이터 전달
): Serializable

interface services {
  @GET("/melon/list/")
  fun getMelonListApi(): Call<Array<SongItem>>
}