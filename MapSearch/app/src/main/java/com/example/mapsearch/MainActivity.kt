package com.example.mapsearch

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.TokenWatcher
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mapsearch.databinding.ActivityMainBinding
import com.example.mapsearch.model.LocationLatLngModel
import com.example.mapsearch.model.SearchModel

class MainActivity : AppCompatActivity() {
  // 1. 뷰바인딩
  private lateinit var binding: ActivityMainBinding
  // 3. adapter 초기화
  private lateinit var adapter: SearchRecyclerAdapter
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    // 1. 뷰바인딩
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)
  
    // 3. adapter 초기화
    initAdapter()
    // 2. 뷰 초기화
    initViews()
    // 4. mocking data 뿌리기
    initData()
    setData()
  }
  
  private fun initViews() = with(binding) {
    emptyResult.isVisible = false
    recyclerView.adapter = adapter
  }
  private fun initAdapter() {
    adapter = SearchRecyclerAdapter()
  }
  @SuppressLint("NotifyDataSetChanged")
  private fun initData() {
    adapter.notifyDataSetChanged()
  }
  private fun setData() {
    val dataList = (0..10).map {
      SearchModel(
        name = "빌딩 $it",
        fullAddress = "주소 $it",
        locationLatLng = LocationLatLngModel(
          it.toDouble(), it.toDouble()
        )
      )
    }
    adapter.submitList(dataList)
  }
}