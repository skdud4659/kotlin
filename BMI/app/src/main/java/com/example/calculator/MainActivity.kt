package com.example.calculator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged

class MainActivity : AppCompatActivity() {
  private var height: Int = 0
  private var weight: Int = 0
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    
    findViewById<EditText>(R.id.height).doAfterTextChanged {
      height = it.toString().toInt()
    }
    findViewById<EditText>(R.id.weight).doAfterTextChanged {
      weight = it.toString().toInt()
    }
    
    findViewById<Button>(R.id.btn_util).setOnClickListener {
      if (height !== 0 && weight !== 0) {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("height", height)
        intent.putExtra("weight", weight)
        startActivity(intent)
      } else {
        Toast.makeText(this, "빈값이 있습니다", Toast.LENGTH_SHORT).show()
      }
    }
  }
}