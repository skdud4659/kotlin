package com.example.clone_instagram

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.google.android.material.tabs.TabLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BoardActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_board)
    
    val viewPager = findViewById<ViewPager2>(R.id.viewPager)
    val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
    
    tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.btn_outsta_home))
    tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.btn_outsta_post))
    tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.btn_outsta_my))
    
    viewPager.adapter = ViewPagerAdapter(this, 3)
    tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
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
  fragmentActivity: FragmentActivity,
  val tabCount: Int
): FragmentStateAdapter(fragmentActivity) {
  override fun getItemCount(): Int {
    return tabCount
  }
  
  override fun createFragment(position: Int): Fragment {
    when(position) {
      0 -> return FeedFragment()
      1 -> return PostFragment()
      else -> return MyFragment()
    }
  }
}


