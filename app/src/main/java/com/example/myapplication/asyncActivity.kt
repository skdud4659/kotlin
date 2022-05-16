package com.example.myapplication

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView

class asyncActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_async)
  }
}

class BackgroundAsyncTask(
  val progressbar: ProgressBar,
  val progressText: TextView
): AsyncTask<Int, Int, Int>() {
  // deprecated > 더 이상 사용을 권장하지 않는다.
  // 코루틴으로 해결 가능. > 특정 기능을 뭉쳐서 명칭.
}