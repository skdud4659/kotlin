package com.example.mapsearch.utility

import com.example.mapsearch.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitUtil {
  
  val apiService: ApiService by lazy { getRetrofit().create(ApiService::class.java) }
  
  // retrofit 전역 생성.
  private fun getRetrofit(): Retrofit {
    return Retrofit.Builder()
      .baseUrl(Url.TMAP_URL)
      .addConverterFactory(GsonConverterFactory.create())
      .client(buildOkHttpClient())
      .build()
  }
  
  // interceptor를 통해 좀 더 편리하게 보기위해 같이 사용.
  private fun buildOkHttpClient(): OkHttpClient {
    val interceptor = HttpLoggingInterceptor()
    if (BuildConfig.DEBUG) {
      interceptor.level = HttpLoggingInterceptor.Level.BODY
    } else {
      interceptor.level = HttpLoggingInterceptor.Level.NONE
    }
    return OkHttpClient.Builder()
      .connectTimeout(5, TimeUnit.SECONDS)
      .addInterceptor(interceptor)
      .build()
  }
}