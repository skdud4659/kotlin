package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout

class tabLayoutPagerActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_tab_layout_pager)
    
    val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
    val viewPager = findViewById<ViewPager2>(R.id.viewPager)
    
    // 탭 등록하기
    tabLayout.addTab(tabLayout.newTab().setText("1"))
    tabLayout.addTab(tabLayout.newTab().setText("2"))
    tabLayout.addTab(tabLayout.newTab().setText("3"))
   
   // pager에 adapter장착
   viewPager.adapter = FragmentPagerAdapter(this@tabLayoutPagerActivity, 3)
  }
}

// activity는 하나고 아래 내용만 변경이 되는 부분으로 fragment를 사용.
class FragmentPagerAdapter(
  fragmentActivity: FragmentActivity,
  val tabCnt: Int
): FragmentStateAdapter(fragmentActivity) {
  override fun getItemCount(): Int {
    return tabCnt
  }
  
  override fun createFragment(position: Int): Fragment {
    when (position) {
      0 -> return Fragment_one()
      1 -> return Fragment_one()
      else -> return Fragment_one()
    }
  }
}