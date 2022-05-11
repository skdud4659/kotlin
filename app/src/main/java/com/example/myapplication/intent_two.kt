package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView

class intent_two : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intent_two)
        
        (findViewById<TextView>(R.id.intent2)).apply {
            this.setOnClickListener {
                 // 화면 전환
//                startActivity(
//                    Intent(this@intent_two, intent_one::class.java)
//                )
                
                // 데이터 전달하기
                val intent: Intent = Intent()
                intent.putExtra("result", "success")
                setResult(RESULT_OK, intent)
                // 액티비티 종료 > 이 전 단계로 돌아감.
                finish()
            }
        }
        
        // intent_one activity(출발지)에서 가져온 데이터
        val intent = intent
        // 넘긴 데이터의 타입에 맞춰서 get~ > getString, getInt ...
        val data : String? = intent.extras?.getString("data")
        if(data !== null) {
            Log.d("data", data)
        }
    }
}