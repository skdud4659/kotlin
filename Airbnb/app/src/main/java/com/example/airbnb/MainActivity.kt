package com.example.airbnb

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import com.naver.maps.map.widget.LocationButtonView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), OnMapReadyCallback, Overlay.OnClickListener {
  // lazy : val을 사용하는 경우, 선언과 동시에 초기화하고 호출 시 초기화, 널 허용 > 반드시 초기화를 안해도 되는 경우 / 최초 초기화 후 다시 초기화 할일이 없을 때 사용.
  private val mapView: MapView by lazy {findViewById(R.id.mapView)}
  private val viewPager: ViewPager2 by lazy { findViewById(R.id.houseViewPager) }
  private val recyclerView: RecyclerView by lazy { findViewById(R.id.recyclerView) }
  private val currentLocationButton: LocationButtonView by lazy { findViewById(R.id.currentLocationButton)}
  private val bottomSheetTitleTextView: TextView by lazy { findViewById(R.id.bottomSheetTitleTextView) }
  private val viewPagerAdapter = HouseViewPagerAdapter(itemClicked = {
    val intent = Intent()
      .apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, "[지금 이 가격에 예약하세요!!!] ${it.title} ${it.price} 사진보기: ${it.imgUrl}")
        // 해당 type을 받는 공유 앱들이 열림.
        type = "text/plain"
      }
    startActivity(Intent.createChooser(intent, null))
  })
  private val recyclerAdapter = HouseListAdapter()
  // lateinit : var을 사용하는 경우, 초기화 후 사용, null을 허용하지 않음, get() set() 사용이 불가능
  private lateinit var naverMap: NaverMap
  private lateinit var locationSource: FusedLocationSource
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    mapView.onCreate(savedInstanceState)
    
    mapView.getMapAsync(this)
    
    // viewpager
    viewPager.adapter = viewPagerAdapter
    
    // recyclerView
    recyclerView.adapter = recyclerAdapter
    recyclerView.layoutManager = LinearLayoutManager(this)
    
    // viewpager가 이동이 되었을 때 호출이 되는 함수
    viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
      override fun onPageSelected(position: Int) {
        super.onPageSelected(position)
        val selectedHouseModel = viewPagerAdapter.currentList[position]
        // 화면 이동
        val cameraUpdate = CameraUpdate.scrollTo(LatLng(selectedHouseModel.lat, selectedHouseModel.lng))
          .animate(CameraAnimation.Easing)
        
        naverMap.moveCamera(cameraUpdate)
      }
    })
  }
  
  // 지도가 다 그려짐
  override fun onMapReady(map: NaverMap) {
    naverMap = map
    // 줌 핸들링
    naverMap.maxZoom = 18.0
    naverMap.minZoom = 10.0
    
    // 내 위치 찍기
    val cameraUpdate = CameraUpdate.scrollTo(LatLng(37.490224, 127.012631))
    naverMap.moveCamera(cameraUpdate)
    
    // 현위치 버튼
    val uiSetting = naverMap.uiSettings
    // true이면 현위치 버튼이 보이지만 서비스 특성 상 viewPager에 가려지기 때문에 별도의 현위치 버튼을 만들어 연결.
    uiSetting.isLocationButtonEnabled = false
    currentLocationButton.map = naverMap
    // 내위치 권한 받기
    locationSource = FusedLocationSource(this@MainActivity, LOCATION_PERMISSION_REQUEST_CODE)
    naverMap.locationSource = locationSource
    
    // 마커 찍기
//    val marker = Marker()
//    marker.position = LatLng(37.490224, 127.012631)
//    marker.map = naverMap
//    marker.icon = MarkerIcons.PINK
//    marker.width = 60
//    marker.height = 80
    
    // api 호출을 여기서 하는 이유는 지도가 생성된 후 마커가 찍혀야하기 때문! > 동기적으로 진행.
    getHouseListFromAPI()
  }
  
  private fun getHouseListFromAPI() {
    // retrofit 생성
    val retrofit = Retrofit.Builder()
      .baseUrl("https://run.mocky.io/")
      .addConverterFactory(GsonConverterFactory.create())
      .build()
    
    // 호출
    retrofit.create(HouseService::class.java).also {
      it.getHouseList()
        .enqueue(object: Callback<HouseDto> {
          // 성공 처리에 대한 구현
          override fun onResponse(call: Call<HouseDto>, response: Response<HouseDto>) {
            if (response.isSuccessful.not()) {
              return
            }
            // 데이터에 마커 찍기
            response.body()?.let {dto ->
              updateMarker(dto.items)
              viewPagerAdapter.submitList(dto.items)
              recyclerAdapter.submitList(dto.items)
              bottomSheetTitleTextView.text = "${dto.items.size}개의 숙소"
            }
          }
  
          // 실패 처리에 대한 구현
          override fun onFailure(call: Call<HouseDto>, t: Throwable) {
            TODO("Not yet implemented")
          }
        })
    }
  }
  private fun updateMarker(houses: List<HouseModel>) {
    houses.forEach {house ->
      val marker = Marker()
      marker.position = LatLng(house.lat, house.lng)
      marker.onClickListener = this
      marker.map = naverMap
      marker.tag = house.id
      marker.icon = MarkerIcons.PINK
      marker.width = 60
      marker.height = 80
    }
  }
  // 위치 권한 결과 함수.
  override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<out String>,
    grantResults: IntArray
  ) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
      return
    }
    if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
      if (!locationSource.isActivated) {
        naverMap.locationTrackingMode = LocationTrackingMode.None
      }
      return
    }
  }
  
  override fun onStart() {
    super.onStart()
    mapView.onStart()
  }
  override fun onResume() {
    super.onResume()
    mapView.onResume()
  }
  override fun onPause() {
    super.onPause()
    mapView.onPause()
  }
  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    mapView.onSaveInstanceState(outState)
  }
  override fun onStop() {
    super.onStop()
    mapView.onStop()
  }
  override fun onDestroy() {
    super.onDestroy()
    mapView.onDestroy()
  }
  override fun onLowMemory() {
    super.onLowMemory()
    mapView.onLowMemory()
  }
  
  companion object {
    private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
  }
  
  // viewPager와 marker 연결 함수.
  override fun onClick(overay: Overlay): Boolean {
    val selectedModel = viewPagerAdapter.currentList.firstOrNull {
      it.id == overay.tag
    }
    selectedModel?.let {
      val position = viewPagerAdapter.currentList.indexOf(it)
      viewPager.currentItem = position
    }
    return true
  }
}