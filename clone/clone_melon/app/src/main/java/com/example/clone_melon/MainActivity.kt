package com.example.clone_melon

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.Serializable

class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    
    val retrofit = Retrofit.Builder()
      .baseUrl("http://mellowcode.org/")
      .addConverterFactory(GsonConverterFactory.create())
      .build()
    val retrofitService = retrofit.create(services::class.java)
    
    retrofitService.getMelonListApi().enqueue(object : Callback<Array<SongItem>> {
      override fun onResponse(call: Call<Array<SongItem>>, response: Response<Array<SongItem>>) {
        val list = response.body()
        findViewById<RecyclerView>(R.id.melon_list_view).apply {
          this.adapter = Adapter(
            list!!,
            LayoutInflater.from(this@MainActivity),
            Glide.with(this@MainActivity),
            this@MainActivity
          )
        }
      }
  
      override fun onFailure(call: Call<Array<SongItem>>, t: Throwable) {
        Log.d("melon", "fail")
      }
    })
  }
}

class Adapter(
  val list: Array<SongItem>,
  val inflater: LayoutInflater,
  val glide: RequestManager,
  val context: Context
): RecyclerView.Adapter<Adapter.ViewHolder>() {
  inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val thumbnail: ImageView
    val title: TextView
    val btnPlay: ImageView
    
    init {
      thumbnail = itemView.findViewById(R.id.thumbnail)
      title = itemView.findViewById(R.id.title)
      btnPlay = itemView.findViewById(R.id.play)
      
      btnPlay.setOnClickListener {
        val intent = Intent(context, DetailActivity::class.java)
        intent.putExtra("list", list as Serializable)
        intent.putExtra("position", adapterPosition)
        context.startActivity(intent)
      }
    }
  }
  
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ViewHolder(
      inflater.inflate(R.layout.item_view, parent, false)
    )
  }
  
  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.title.text = list.get(position).title
    glide.load(list.get(position).thumbnail).centerCrop().into(holder.thumbnail)
  }
  
  override fun getItemCount(): Int {
    return list.size
  }
}