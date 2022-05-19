package com.example.clone_instagram

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FeedFragment: Fragment() {
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_feed, container, false)
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    
    val retrofit = Retrofit.Builder()
      .baseUrl("http://mellowcode.org/")
      .addConverterFactory(GsonConverterFactory.create())
      .build()
    val retrofitService = retrofit.create(Services::class.java)
  
    retrofitService.getPost().enqueue(object : Callback<Array<PostInfo>> {
      override fun onResponse(call: Call<Array<PostInfo>>, response: Response<Array<PostInfo>>) {
        val list = response.body()
        val recyclerView = view.findViewById<RecyclerView>(R.id.feed_list)
        recyclerView.adapter = RecyclerViewAdapter(
          list!!,
          LayoutInflater.from(activity),
          Glide.with(activity!!)
        )
      }
    
      override fun onFailure(call: Call<Array<PostInfo>>, t: Throwable) {
        showAlert("" + t, activity!!, LayoutInflater.from(activity))
      }
    })
  }
  class RecyclerViewAdapter(
    val list: Array<PostInfo>,
    val inflater: LayoutInflater,
    val glide: RequestManager
  ): RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
      var writerProfile: ImageView
      var writerName: TextView
      var postImage: ImageView
      var content: TextView
      
      init {
        writerProfile = itemView.findViewById(R.id.writer_profile)
        writerName = itemView.findViewById(R.id.writer_name)
        postImage = itemView.findViewById(R.id.post_image)
        content = itemView.findViewById(R.id.content)
      }
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
      return ViewHolder(inflater.inflate(R.layout.post_item, parent, false))
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      val post = list.get(position)
      holder.writerName.text = post.owner_profile.username
      holder.content.text = post.content
      post.owner_profile.image.let{
        glide.load(it).centerCrop().circleCrop().into(holder.writerProfile)
      }
      post.image.let{
        glide.load(it).centerCrop().into(holder.postImage)
      }
      
    }
    
    override fun getItemCount(): Int {
      return list.size
    }
  }
}


