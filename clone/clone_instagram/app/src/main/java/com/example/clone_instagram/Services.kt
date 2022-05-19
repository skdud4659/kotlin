package com.example.clone_instagram

import retrofit2.Call
import retrofit2.http.*

class Token (val token: String)
class SignUpInfo (val username: String, val token: String)
class owner_profile (val id: Int, val username: String, val image: String, val user: Int)
class PostInfo (val id: Int, val created: String, val content: String, val image: String, val like_count: Int, val owner_profile: owner_profile)

interface Services {
  @POST("/user/signup/")
  @FormUrlEncoded
  fun postSignUp(
    @Field("username") username: String,
    @Field("password1") password1: String,
    @Field("password2") password2: String,
  ): Call<SignUpInfo>
  
  @POST("/user/login/")
  @FormUrlEncoded
  fun postLogin(
    @Field("username") username: String,
    @Field("password") password: String,
  ): Call<Token>
  
  @GET("/instagram/post/list/all/")
  fun getPost(): Call<Array<PostInfo>>
}