package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class stack_three : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_stack_three)
  
    (findViewById<TextView>(R.id.one)).setOnClickListener {
      startActivity(Intent(this@stack_three, stack_one::class.java))
    }
    (findViewById<TextView>(R.id.two)).setOnClickListener {
      startActivity(Intent(this@stack_three, stack_two::class.java))
    }
    (findViewById<TextView>(R.id.three)).setOnClickListener {
      startActivity(Intent(this@stack_three, stack_three::class.java))
    }
  }
}