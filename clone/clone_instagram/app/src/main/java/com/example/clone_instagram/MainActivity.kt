package com.example.clone_instagram

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.widget.doAfterTextChanged
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
  var username: String = ""
  var password: String = ""
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    
    findViewById<TextView>(R.id.to_signup).setOnClickListener {
      startActivity(Intent(this, SignUpActivity::class.java))
    }
    findViewById<EditText>(R.id.id).doAfterTextChanged {
      username = it.toString()
    }
    findViewById<EditText>(R.id.pw).doAfterTextChanged {
      password = it.toString()
    }
    findViewById<TextView>(R.id.btn_login).setOnClickListener {
      val retrofit = Retrofit.Builder()
        .baseUrl("http://mellowcode.org")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
      val retrofitService = retrofit.create(Services::class.java)
      if (username === "" || password === "") {
        showAlert("빈칸을 입력해주세요.", this, LayoutInflater.from(this))
      } else {
        retrofitService.postLogin(username, password).enqueue(object: Callback<Token>{
          override fun onResponse(call: Call<Token>, response: Response<Token>) {
            if (response.isSuccessful) {
              val token: Token = response.body()!!
              val sharedPreferences = getSharedPreferences("user_token", Context.MODE_PRIVATE)
              val editor: SharedPreferences.Editor = sharedPreferences.edit()
              editor.putString("token", token.token)
              editor.commit()
              startActivity(Intent(this@MainActivity, BoardActivity::class.java))
            }
          }
    
          override fun onFailure(call: Call<Token>, t: Throwable) {
            showAlert("" + t, this@MainActivity, LayoutInflater.from(this@MainActivity))
          }
        })
      }
    }
  }
}

fun showAlert(text: String, context: Context, inflater: LayoutInflater) {
  val view = inflater.inflate(R.layout.alert, null)
  val alertText: TextView = view.findViewById(R.id.alert_text)
  alertText.text = text
  
  val alertDialog = AlertDialog.Builder(context)
    .setTitle("로그인 실패")
    .setPositiveButton("확인", null)
    .create()
  alertDialog.setView(view)
  alertDialog.show()
}