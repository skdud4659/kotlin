package com.example.calculate

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.room.Room
import com.example.calculate.model.History
import java.lang.NumberFormatException

class MainActivity : AppCompatActivity() {
    private val expressionText: TextView by lazy { findViewById(R.id.expression) }
    private val resultText: TextView by lazy { findViewById(R.id.resultText) }
    private val historyView: View by lazy { findViewById(R.id.historyLayout) }
    private val historyViewInner: LinearLayout by lazy { findViewById(R.id.historyInner) }

    private var isOps: Boolean = false
    private var hasOps: Boolean = false

    lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "historyDB").build()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun buttonClicked(view: View) {
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
        resultText.text = calculateExpression()
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
            expressionText.text.length - 1,
            expressionText.text.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        expressionText.text = ssb
        isOps = true
        hasOps = true
    }


    fun resultButtonClicked(view: View) {
        val expression = expressionText.text.toString()
        val result = calculateExpression()

        // db
        Thread(Runnable {
            db.historyDao().insertHistory(History(null, expression, result))
        }).start()

        expressionText.text = resultText.text
        resultText.text = ""
    }

    private fun calculateExpression(): String {
        val expressionTexts = expressionText.text.split(" ")
        if (hasOps.not() || expressionTexts.size !== 3) {
            return ""
        } else if (expressionTexts[0].isNumber().not() || expressionTexts[2].isNumber().not()) {
            return ""
        }
        val exp1 = expressionTexts[0].toBigInteger()
        val exp2 = expressionTexts[2].toBigInteger()
        val op = expressionTexts[1]

        return when (op) {
            "+" -> (exp1 + exp2).toString()
            "-" -> (exp1 - exp2).toString()
            "*" -> (exp1 * exp2).toString()
            "/" -> (exp1 / exp2).toString()
            "%" -> (exp1 % exp2).toString()
            else -> ""
        }
    }

    fun clearButtonClicked(view: View) {
        expressionText.text = ""
        resultText.text = ""
        isOps = false
        hasOps = false
    }

    fun historyButtonClicked(view: View) {
        historyView.isVisible = true
        historyViewInner.removeAllViews()
        Thread(Runnable {
            db.historyDao().getAll().reversed().forEach {
                runOnUiThread{
                    val historyView = LayoutInflater.from(this).inflate(R.layout.history_row, null, false)
                    historyView.findViewById<TextView>(R.id.expression).text = it.expression
                    historyView.findViewById<TextView>(R.id.resultText).text = "= ${it.result}"
                    historyViewInner.addView(historyView)
                }
            }
        }).start()
    }

    fun closeHistoryBtn(view: View) {
        historyView.isVisible = false
    }

    fun historyDelBtn(view: View) {
        historyViewInner.removeAllViews()
        Thread(Runnable {
            db.historyDao().delAll()
        }).start()
    }
}

// 확장함수
fun String.isNumber(): Boolean {
    return try {
        this.toBigInteger()
        true
    } catch (e: NumberFormatException) {
        false
    }
}