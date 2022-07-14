package com.example.airbnb

import retrofit2.Call
import retrofit2.http.GET

interface HouseService {
  @GET("/v3/8a3d194c-b42b-4b1f-bbbb-492c9d03050f")
  fun getHouseList(): Call<HouseDto>
}