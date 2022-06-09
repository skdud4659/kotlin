package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView

class FragmentActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_fragment)
    
    val fragmentManager = supportFragmentManager
    val fragmentOne = Fragment_one()
    findViewById<TextView>(R.id.add).setOnClickListener {
      // transaction > 작업의 단위(시작과 끝이 있다.)
      val transaction = fragmentManager.beginTransaction() // 시작
      
      // fragment에 데이터 전달하는 방법(부모 > 자식) > 번들을 만들어 번들에 데이터를 넣은 후 tansaction에 장착하여 전달
      // 번들 생성
      val bundle = Bundle()
      // 번들에 데이터 추가
      bundle.putString("data", "fragment로 데이터 전달하기!")
      // 번들을 fragment의 argument로 등록
      fragmentOne.arguments = bundle
      transaction.replace(R.id.root, fragmentOne, "fragment_first_tag")
      transaction.commit() // 끝
    }
  
    findViewById<TextView>(R.id.remove).setOnClickListener {
      // transaction > 작업의 단위(시작과 끝이 있다.)
      val transaction = fragmentManager.beginTransaction() // 시작
      transaction.remove(fragmentOne)
      transaction.commit() // 끝
    }
    
    findViewById<TextView>(R.id.access_fragment).setOnClickListener {
      // XML에 있는 fragment를 찾는 방법
//     val fragmentFirst = supportFragmentManager.findFragmentById(R.id.fragment_activity) as Fragment_one
//      fragmentFirst.printTestLog()
      
      // XML에 없는 fragment를 찾는 방법 > 코드상 붙였을 경우 태그를 단 fragment를 표출해두고 call 해야 작동.
      val fragmentFirst = supportFragmentManager.findFragmentByTag("fragment_first_tag") as Fragment_one
      fragmentFirst.printTestLog()
    }
  }
  
  // 자식이 상속받을 함수
  fun printTestLog() {
    Log.d("testt", "print test log")
  }
}