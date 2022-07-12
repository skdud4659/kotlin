package com.example.carrot

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.carrot.chatList.ChatListFragment
import com.example.carrot.home.HomeFragment
import com.example.carrot.myPage.MyPageFragement
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    
    val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
    val homeFragment = HomeFragment()
    val chatListFragment = ChatListFragment()
    val myPageFragment = MyPageFragement()
    
    // 초깃값 셋팅
    replaceFragment(homeFragment)
    
    // 하단 네이게이션 클릭 이벤트
    bottomNavigationView.setOnItemSelectedListener {
      when (it.itemId) {
        R.id.home -> replaceFragment(homeFragment)
        R.id.chatList -> replaceFragment(chatListFragment)
        R.id.myPage ->replaceFragment(myPageFragment)
      }
      true
    }
  }
  // fragment 교체 이벤트
  private fun replaceFragment(fragment: Fragment) {
    supportFragmentManager.beginTransaction()
      .apply {
        replace(R.id.fragment_container, fragment)
        commit()
      }
  }
}