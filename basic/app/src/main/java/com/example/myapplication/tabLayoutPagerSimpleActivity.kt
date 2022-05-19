package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout

// fragment를 쓰지 않고 심플 버전.

class tabLayoutPagerSimpleActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_tab_layout_pager_simple)
    
    val viewPager = findViewById<ViewPager>(R.id.viewPager)
    val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
  
    // 탭 등록하기
    tabLayout.addTab(tabLayout.newTab().setText("1"))
    tabLayout.addTab(tabLayout.newTab().setText("2"))
    tabLayout.addTab(tabLayout.newTab().setText("3"))
    
    // 어답터 장착
    viewPager.adapter = ViewPagerAdapter(LayoutInflater.from(this),3)
    tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
      override fun onTabSelected(tab: TabLayout.Tab?) {
        viewPager.setCurrentItem((tab!!.position))
      }
  
      override fun onTabUnselected(tab: TabLayout.Tab?) {
      }
  
      override fun onTabReselected(tab: TabLayout.Tab?) {
      }
    })
  }
}

class ViewPagerAdapter(
  val layoutInflater: LayoutInflater,
  val tabCount: Int
): PagerAdapter() {
  override fun getCount(): Int {
    return tabCount
  }
  
  // instantiateItem에서 Return된 뷰를 매개변수로 받음.
  override fun isViewFromObject(view: View, `object`: Any): Boolean {
    return view === `object` as View
  }
  
  override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
    container.removeView(`object` as View)
  }
  
  // = createFragment
  // 보여줄 아이템(뷰)을 리턴
  override fun instantiateItem(container: ViewGroup, position: Int): Any {
    when (position) {
      0 -> {
        val view = layoutInflater.inflate(R.layout.fragment_layout, container,false)
        container.addView(view)
        return view
      }
      1 -> {
        val view = layoutInflater.inflate(R.layout.activity_intent_one, container,false)
        container.addView(view)
        return view
      }
      else -> {
        val view = layoutInflater.inflate(R.layout.activity_intent_two, container,false)
        container.addView(view)
        return view
      }
    }
  }
}