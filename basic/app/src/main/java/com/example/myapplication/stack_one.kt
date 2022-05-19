package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import org.w3c.dom.Text

class stack_one : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_stack_one)
    
    (findViewById<TextView>(R.id.one)).setOnClickListener {
      startActivity(Intent(this@stack_one, stack_one::class.java))
    }
    (findViewById<TextView>(R.id.two)).setOnClickListener {
      startActivity(Intent(this@stack_one, stack_two::class.java))
    }
    (findViewById<TextView>(R.id.three)).setOnClickListener {
      startActivity(Intent(this@stack_one, stack_three::class.java))
    }
  }
}