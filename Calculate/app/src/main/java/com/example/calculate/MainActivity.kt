package com.example.calculate

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi

class MainActivity : AppCompatActivity() {
    private val expressionText: TextView by lazy { findViewById(R.id.expression) }
    private val resultText: TextView by lazy { findViewById(R.id.resultText) }
    private var isOps: Boolean = false
    private var hasOps: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun buttonClicked(view: View) {
        when (view.id) {
            R.id.btn0 -> numButtonClicked("0")
            R.id.btn1 -> numButtonClicked("1")
            R.id.btn2 -> numButtonClicked("2")
            R.id.btn3 -> numButtonClicked("3")
            R.id.btn4 -> numButtonClicked("4")
            R.id.btn5 -> numButtonClicked("5")
            R.id.btn6 -> numButtonClicked("6")
            R.id.btn7 -> numButtonClicked("7")
            R.id.btn8 -> numButtonClicked("8")
            R.id.btn9 -> numButtonClicked("9")
            R.id.btnPlus -> opsButtonClicked("+")
            R.id.btnDivider -> opsButtonClicked("/")
            R.id.btnPer -> opsButtonClicked("%")
            R.id.btnMinus -> opsButtonClicked("-")
            R.id.btnTimes -> opsButtonClicked("*")
        }
    }

    private fun numButtonClicked(number: String) {
        if (isOps) {
            expressionText.append(" ")
        }
        isOps = false
        val expression = expressionText.text.split(" ")
        if (expression.isNotEmpty() && expression.last().length >= 15) {
            Toast.makeText(this, "15자리까지만 사용할 수 있습니다.", Toast.LENGTH_SHORT).show()
            return
        } else if (expression.last().isEmpty() && number == "0") {
            Toast.makeText(this, "0은 제일 앞에 올 수 없습니다.", Toast.LENGTH_SHORT).show()
            return
        }
        expressionText.append(number)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun opsButtonClicked(ops: String) {
        if (expressionText.text.isEmpty()) {
            return
        }
        when {
            isOps -> {
                val text = expressionText.text.toString()
                expressionText.text = text.dropLast(1) + ops
            }
            hasOps -> {
                Toast.makeText(this, "연산자는 한 번만 사용할 수 있습니다.", Toast.LENGTH_SHORT).show()
                return
            }
            else -> {
                expressionText.append(" $ops")
            }
        }
        val ssb = SpannableStringBuilder(expressionText.text)
        ssb.setSpan(
            ForegroundColorSpan(getColor(R.color.green)),
            expressionText.text.length -1,
            expressionText.text.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        expressionText.text = ssb
        isOps = true
        hasOps = true
    }

    private fun historyButtonClicked(view: View) {

    }

    private fun resultButtonClicked(view: View) {

    }

    private fun clearButtonClicked(view: View) {

    }
}