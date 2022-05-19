package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView

class activity_view_control : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_control)

        // 뷰를 코틀린 파일로 가져오는 방법
        val textViewOne: TextView = findViewById(R.id.textViewOne)
        val buttonOne: Button = findViewById(R.id.buttonOne)
        Log.d("testt", textViewOne.text.toString())

        // Listener 사용 방법
        buttonOne.setOnClickListener {
            // 버튼이 클릭되었을 때 동작할 코드 작성
            Log.d("testt", "버튼 클릭!")
        }
    }
}