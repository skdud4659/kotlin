package com.example.mapsearch

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.isVisible
import com.example.mapsearch.databinding.ActivityMainBinding
import com.example.mapsearch.model.LocationEntity
import com.example.mapsearch.model.SearchResultEntity
import com.example.mapsearch.response.search.Poi
import com.example.mapsearch.response.search.Pois
import com.example.mapsearch.utility.RetrofitUtil
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

// CoroutineScope를 통해 어떻게 비동기 적으로 구성할건지 정함.
class MainActivity : AppCompatActivity(), CoroutineScope {
  // 코루틴
  private lateinit var job: Job
  // 어떤 쓰레드에서 동작할지 명시
  override val coroutineContext: CoroutineContext
    get() = Dispatchers.Main + job
  
  // 1. 뷰바인딩
  private lateinit var binding: ActivityMainBinding
  // 3. adapter 초기화
  private lateinit var adapter: SearchRecyclerAdapter
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    // 1. 뷰바인딩
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)
    
    // job 객체 초기화
    job = Job()
  
    // 3. adapter 초기화
    initAdapter()
    // 2. 뷰 초기화
    initViews()
    // 바인딩 초기화
    bindViews()
    
    // 4. mocking data 뿌리기
    initData()
//    setData()
  }
  
  private fun initViews() = with(binding) {
    emptyResult.isVisible = false
    recyclerView.adapter = adapter
  }
  private fun bindViews() = with(binding) {
    searchButton.setOnClickListener {
      searchKeyword(searchBarInputView.text.toString())
    }
  }
  private fun searchKeyword(keyword: String) {
    // 비동기 > 메인쓰레드에서 먼저 실행한다.
    launch(coroutineContext) {
      try {
        withContext(Dispatchers.IO) {
          val response = RetrofitUtil.apiService.getSearchLocation(
            keyword = keyword
          )
          if (response.isSuccessful) {
            val body = response.body()
            withContext(Dispatchers.Main) {
              body?.let {searchResponse ->
                setData(searchResponse.searchPoiInfo.pois)
              }
            }
          }
        }
      } catch (e: Exception) {
      
      }
    }
  }
  private fun initAdapter() {
    adapter = SearchRecyclerAdapter()
  }
  @SuppressLint("NotifyDataSetChanged")
  private fun initData() {
    // 리스트의 크기와 아이템이 둘 다 변경되는 경우에 사용
    adapter.notifyDataSetChanged()
  }
  private fun setData(pois: Pois) {
    val dataList = pois.poi.map {
      SearchResultEntity(
        name = it.name ?: "빌딩명 없음",
        fullAddress = makeMainAddress(it),
        locationLatLng = LocationEntity(
          it.noorLat.toDouble(), it.noorLon.toDouble()
        )
      )
    }
    adapter.submitList(dataList)
  }
  private fun makeMainAddress(poi: Poi): String =
    if (poi.secondNo?.trim().isNullOrEmpty()) {
      (poi.upperAddrName?.trim() ?: "") + " " +
          (poi.middleAddrName?.trim() ?: "") + " " +
          (poi.lowerAddrName?.trim() ?: "") + " " +
          (poi.detailAddrName?.trim() ?: "") + " " +
          poi.firstNo?.trim()
    } else {
      (poi.upperAddrName?.trim() ?: "") + " " +
          (poi.middleAddrName?.trim() ?: "") + " " +
          (poi.lowerAddrName?.trim() ?: "") + " " +
          (poi.detailAddrName?.trim() ?: "") + " " +
          (poi.firstNo?.trim() ?: "") + " " +
          poi.secondNo?.trim()
    }
    
}