package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

// Fragment를 상속받아 Fragment 역할을 하게 함.
class Fragment_one: Fragment() {
  // 뷰 생성
  override fun onCreateView(
    // inflater : 작성된 xml을 뷰에 보일 수 있게끔 준비. ( xml > 뷰 )
    inflater: LayoutInflater,
    // container : fragment에서 사용될 xml의 부모뷰
    container: ViewGroup?,
    // savedInstanceState : 사용자가 잠깐 사용하는 기록하는 것들
    savedInstanceState: Bundle?
  ): View? {
    
    // 뷰 만들기
    val view = inflater.inflate(R.layout.fragment_layout, container, false)
    
    // fragment에서 activity로 데이터 전달하기(자식 > 부모)
    view.findViewById<TextView>(R.id.call_activity).setOnClickListener {
      // printTestLog() > 부모에서 함수 생성.
      (activity as FragmentActivity).printTestLog()
    }
    return view
  }
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val data: String? = arguments?.getString("data")
    Log.d("testt", "test" + data)
  }
  
  fun printTestLog() {
    Log.d("testt", "print test log from fragment")
  }
}