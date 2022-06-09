package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class stack_two : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_stack_two)
  
    (findViewById<TextView>(R.id.one)).setOnClickListener {
      startActivity(Intent(this@stack_two, stack_one::class.java))
    }
    (findViewById<TextView>(R.id.two)).setOnClickListener {
      startActivity(Intent(this@stack_two, stack_two::class.java))
    }
    (findViewById<TextView>(R.id.three)).setOnClickListener {
      val intent = Intent(this@stack_two, stack_three::class.java)
      startActivity(intent)
    }
  }
}