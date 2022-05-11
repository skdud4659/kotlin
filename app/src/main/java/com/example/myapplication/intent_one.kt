package com.example.myapplication

import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

class intent_one : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_intent_one)
    
    // 암시적 인텐트
    // 전화, 플레이스토어 이동, 웹사이트로 이동, 구글 맵, sms, 사진첩 등등
    // 상수 : 변하지 않는 수 (문자 포함) > final NAME(변수명이 전부 다 대문자인 경우 상수라고 판단하면됨)
    val implicit_intent : TextView = findViewById(R.id.implicit_intent)
    implicit_intent.setOnClickListener {
      // intent(액션, 전달할 데이터)
      // 데이터는 uri 형식으로 보내야한다.
      val intent: Intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:010-111-1111"))
      startActivity(intent)
    }
    
    // 명시적 인텐트
    // 화면 전환에 많이 사용.
    // 방법1
    val explicit_intent1: TextView = findViewById(R.id.explicit_intent1)
    explicit_intent1.setOnClickListener {
      val intent: Intent = Intent()
      // ComponentName(패키지명, 클래스명)
      val componentName: ComponentName = ComponentName(
        // 하지만 문자열을 사용할 경우 오타 등의 이유로 오류가 생겨도 오류가 뜨지 않는다.
        "com.example.myapplication",
        "com.example.myapplication.intent_two"
      )
      intent.component = componentName
      startActivity(intent)
    }
    
    // 방법2
    (findViewById<TextView>(R.id.explicit_intent2)).apply {
      this.setOnClickListener {
        startActivity(
          // this 가 의미하는 것이 여러개일 경우 @~로 명시해준다.
          Intent(this@intent_one, intent_two::class.java)
        )
      }
    }
    
    // 명시적 인텐트 + 데이터
    (findViewById<TextView>(R.id.intent3)).apply {
      this.setOnClickListener {
          val intent = Intent(this@intent_one, intent_two::class.java)
          intent.putExtra("data", "data1")
          startActivity(intent)
      }
    }
    
    // 명시적 인텐트 + result
    // requestCode > 어느 액티비티로부터 받는 결과값인지 구분 하기 위해서 작성.
    // ActivityResult API
    val startActivityLauncher: ActivityResultLauncher<Intent> =
      registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        when(it.resultCode) {
          RESULT_OK->{
            Log.d("dataa", it.data?.extras?.getString("result")!!)
          }
        }
      }
  
    (findViewById<TextView>(R.id.intent4)).apply {
      this.setOnClickListener {
        val intent = Intent(this@intent_one, intent_two::class.java)
        // 결과를 받는 매체는 intent_one / 결과를 주는 매체는 intent_two
        // startActivityForResult(intent, 1) > deprecated
        // ActivityResult API > requestCode가 필요없음.
        startActivityLauncher.launch(intent)
      }
    }
  }
}