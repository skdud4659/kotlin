package com.example.diary

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.EditText
import androidx.core.content.edit
import androidx.core.widget.addTextChangedListener

class DiaryActivity : AppCompatActivity() {
    // 메인쓰레드에 연결된 핸들러 : 쓰레드간의 통신을 엮어주는 안드로이드에서 제공하는 기능.
    private val handler = Handler(Looper.getMainLooper())

    private val editDiary: EditText by lazy {
        findViewById<EditText>(R.id.editDiary)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary)

        val detailPreference = getSharedPreferences("diary", Context.MODE_PRIVATE)
        editDiary.setText(detailPreference.getString("detail", ""))

        // 쓰레드에서 일어나는 기능
        val runnable = Runnable {
            detailPreference.edit {
                putString("detail", editDiary.text.toString())
            }
        }
        // 수정될때마다 저장
        editDiary.addTextChangedListener {
            // 아래와 같이 코드를 짤 경우 글이 입력될때마다 너무 빈번하게 저장이 되는 문제가 있다.
//            detailPreference.edit {
//                putString("detail", editDiary.text.toString())
//            }
            // 이 전에 있는 runnable 삭제
            handler.removeCallbacks(runnable)
            // 0.5초 후에 runnable 실행
            handler.postDelayed(runnable, 500)
        }
    }
}