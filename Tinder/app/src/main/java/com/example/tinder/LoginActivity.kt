package com.example.tinder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.example.tinder.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        handleLogin()
        handleSignUp()
        handleEditText()
    }
    private fun handleLogin() {
        binding.loginBtn.setOnClickListener {
            val email = getInputEmail()
            val password = getInputPassword()
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        handleSuccessLogin()
                    } else {
                        Toast.makeText(this, "로그인에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
    private fun handleSignUp() {
        binding.signupBtn.setOnClickListener {
            val email = getInputEmail()
            val password = getInputPassword()
            auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "회원가입에 성공했습니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "회원가입에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
                }

        }
    }
    private fun getInputEmail(): String {
        return binding.emailEditText.text.toString()
    }
    private fun getInputPassword(): String {
        return binding.passwordEditText.text.toString()
    }
    private fun handleEditText() {
        val email = binding.emailEditText
        val password = binding.passwordEditText
        email.addTextChangedListener {
            val enable = email.text.isNotEmpty() && password.text.isNotEmpty()
            binding.loginBtn.isEnabled = enable
            binding.signupBtn.isEnabled = enable
        }
        password.addTextChangedListener {
            val enable = email.text.isNotEmpty() && password.text.isNotEmpty()
            binding.loginBtn.isEnabled = enable
            binding.signupBtn.isEnabled = enable
        }
    }
    private fun handleSuccessLogin() {
        if (auth.currentUser == null) {
            Toast.makeText(this, "로그인 실패했습니다.", Toast.LENGTH_SHORT).show()
            return
        }
        val userId = auth.currentUser?.uid.orEmpty()
        // reference: 최상위
        // 없으면 생성, 있으면 가져옴
        val currentUserDB = Firebase.database.reference.child("Users").child(userId)
        val user = mutableMapOf<String, Any>()
        user["userId"] = userId
        currentUserDB.updateChildren(user)
        finish()
    }
}