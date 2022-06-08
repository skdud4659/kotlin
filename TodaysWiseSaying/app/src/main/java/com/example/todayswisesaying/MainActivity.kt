package com.example.todayswisesaying

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import org.json.JSONArray
import org.json.JSONObject
import kotlin.math.absoluteValue

class MainActivity : AppCompatActivity() {
  private val viewPager: ViewPager2 by lazy { findViewById(R.id.viewPager) }
  private val progressBar: ProgressBar by lazy { findViewById(R.id.progressBar) }


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    
    initData()
    initViews()
  }

  private fun initViews() {
    // 페이지 전환효과
    viewPager.setPageTransformer { page, position ->
      // transition, alpha, scale
      when {
        // absoluteValue : 절대값
        position.absoluteValue >= 1F -> {
          page.alpha = 0F
        }
        position == 0F -> {
          page.alpha = 1F
        }
        else -> {
          page.alpha = 1F - 2 * position.absoluteValue
        }
      }
    }
  }
  private fun initData() {
    val remoteConfig = Firebase.remoteConfig
    remoteConfig.setConfigSettingsAsync(
      remoteConfigSettings {
        minimumFetchIntervalInSeconds = 0
      }
    )
    remoteConfig.fetchAndActivate().addOnCompleteListener {
      progressBar.visibility = View.GONE
      if (it.isSuccessful) {
        val quotes = parseQuoteJson(remoteConfig.getString("quotes"))
        val isNameRevealed = remoteConfig.getBoolean("is_name_revealed")
  
        displayQuotesPager(quotes, isNameRevealed)
      }
    }
  }
  
  private fun displayQuotesPager(quotes: List<Quote>, isNameRevealed: Boolean) {
    val adapter = QuoteAdapter(
      quotes = quotes,
      isNameRevealed = isNameRevealed
    )
    viewPager.apply {
      this.adapter = adapter
      // 왼쪽으로도 무한 스와이프 적용.
      this.setCurrentItem(adapter.itemCount/2, false)
    }
  }
  
  private fun parseQuoteJson(json: String): List<Quote> {
    val jsonArray = JSONArray(json)
    var jsonList = emptyList<JSONObject>()
    
    for (index in 0 until jsonArray.length()) {
      val jsonObject = jsonArray.getJSONObject(index)
      jsonObject?.let {
        jsonList = jsonList + it
      }
    }
    return jsonList.map {
      Quote(
        quote = it.getString("quote"),
        name = it.getString("name"))
    }
  }
}