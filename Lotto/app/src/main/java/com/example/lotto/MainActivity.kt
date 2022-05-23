package com.example.lotto

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible

class MainActivity : AppCompatActivity() {
  private val resetButton: Button by lazy {
    findViewById<Button>(R.id.btn_reset)
  }
  private val addButton: Button by lazy {
    findViewById<Button>(R.id.btn_add)
  }
  private val startButton: Button by lazy {
    findViewById<Button>(R.id.btn_start)
  }
  private val numberPicker: NumberPicker by lazy {
    findViewById<NumberPicker>(R.id.number_picker)
  }
  private val numberTextList: List<TextView> by lazy {
    listOf<TextView>(
      findViewById<TextView>(R.id.textView1),
      findViewById<TextView>(R.id.textView2),
      findViewById<TextView>(R.id.textView3),
      findViewById<TextView>(R.id.textView4),
      findViewById<TextView>(R.id.textView5),
      findViewById<TextView>(R.id.textView6)
    )
  }
  private var didRun = false
  private val pickNumberSet = hashSetOf<Int>()
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    
    // numberPicker는 범위가 있어야 시뮬레이터에 나옴.
    numberPicker.minValue = 1
    numberPicker.maxValue = 45
    
    initStartButton()
    initAddButton()
    initResetButton()
  }
  
  private fun initResetButton() {
    resetButton.setOnClickListener {
      didRun = false
      pickNumberSet.clear()
      numberTextList.forEach {
        it.isVisible = false
      }
      numberPicker.value = 1
    }
  }
  private fun initAddButton() {
    addButton.setOnClickListener {
      if (didRun) {
        Toast.makeText(this, "초기화 후에 시도해주세요.", Toast.LENGTH_SHORT).show()
        return@setOnClickListener
      }
      
      if (pickNumberSet.size >= 6) {
        Toast.makeText(this, "번호는 최대 6개까지 선택이 가능합니다.", Toast.LENGTH_SHORT).show()
        return@setOnClickListener
      }
      
      if (pickNumberSet.contains(numberPicker.value)) {
        Toast.makeText(this, "이미 선택된 번호입니다.", Toast.LENGTH_SHORT).show()
        return@setOnClickListener
      }
      
      val textView = numberTextList[pickNumberSet.size]
      textView.isVisible = true
      textView.text = numberPicker.value.toString()
      setBackground(numberPicker.value, textView)
      
      pickNumberSet.add(numberPicker.value)
    }
  }
  
  private fun initStartButton() {
    startButton.setOnClickListener{
      didRun = true
      val list = getRandomNumber()
      list.forEachIndexed { index, number ->
        val textView = numberTextList[index]
        textView.text = number.toString()
        textView.isVisible = true
        setBackground(number, textView)
      }
    }
  }
  
  private fun setBackground(number: Int, textView: TextView) {
    when(number) {
      in 1..10 -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_yellow)
      in 11..20 -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_blue)
      in 21..30 -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_red)
      in 31..40 -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_gray)
      else -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_green)
    }
  }
  private fun getRandomNumber(): List<Int> {
    val numberList = mutableListOf<Int>().apply {
      for (i in 1..45) {
        if (pickNumberSet.contains(i)) {
          continue
        }
        this.add(i)
      }
    }
    // 무작위로 섞음
    numberList.shuffle()
    // 6번째까지 자름
    val newList = pickNumberSet.toList() + numberList.subList(0, 6 - pickNumberSet.size)
    // 오름차순으로 정렬
    return newList.sorted()
  }
}