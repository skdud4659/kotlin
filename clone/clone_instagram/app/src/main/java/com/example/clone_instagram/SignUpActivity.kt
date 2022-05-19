package com.example.clone_instagram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SignUpActivity : AppCompatActivity() {
  var username: String = ""
  var password1: String = ""
  var password2: String = ""
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_sign_up)
  
    findViewById<EditText>(R.id.id).doAfterTextChanged {
      username = it.toString()
    }
    findViewById<EditText>(R.id.pw).doAfterTextChanged {
      password1 = it.toString()
    }
    findViewById<EditText>(R.id.pw_cfm).doAfterTextChanged {
      password2 = it.toString()
    }
    findViewById<TextView>(R.id.btn_signUp).setOnClickListener {
      val retrofit = Retrofit.Builder()
        .baseUrl("http://mellowcode.org")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
      val retrofitService = retrofit.create(Services::class.java)
      if (username === "" || password1 === "" || password2 === "") {
        showAlert("빈칸을 입력해주세요.", this, LayoutInflater.from(this))
      } else {
        if (password1 == password2) {
          retrofitService.postSignUp(username, password1, password2).enqueue(object : Callback<SignUpInfo>{
            override fun onResponse(call: Call<SignUpInfo>, response: Response<SignUpInfo>) {
              if (response.isSuccessful) {
                startActivity(Intent(this@SignUpActivity, MainActivity::class.java))
              }
            }
    
            override fun onFailure(call: Call<SignUpInfo>, t: Throwable) {
              showAlert("" + t, this@SignUpActivity, LayoutInflater.from(this@SignUpActivity))
            }
          })
        } else {
          showAlert("비밀번호를 확인해주세요.", this@SignUpActivity, LayoutInflater.from(this@SignUpActivity))
        }
      }
    }
  }
}