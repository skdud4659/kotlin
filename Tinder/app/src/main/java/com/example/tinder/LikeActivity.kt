package com.example.tinder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LikeActivity : AppCompatActivity() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var userDB: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_like)

        userDB  = Firebase.database.reference.child("Users")
        val currentUserDB = userDB.child(getCurrentUserId())
        currentUserDB.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child("name").value == null) {
                    showNameInputPopup()
                    return
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
    private fun getCurrentUserId(): String {
        if (auth.currentUser == null) {
            Toast.makeText(this, "로그인을 해주세요.", Toast.LENGTH_SHORT).show()
            finish()
        }
        return auth.currentUser?.uid.orEmpty()
    }
    private fun showNameInputPopup() {
        val nameEditText = EditText(this)
        AlertDialog.Builder(this)
            .setTitle("이름을 입력해주세요")
            .setView(nameEditText)
            .setPositiveButton("저장") {_, _ ->
                if (nameEditText.text.isEmpty()) {
                    showNameInputPopup()
                } else {
                    saveUserName(nameEditText.text.toString())
                }
            }
            .setCancelable(false)
            .show()
    }
    private fun saveUserName(name: String) {
        val userId = getCurrentUserId()
        val currentUserDB = userDB.child(userId)
        val user = mutableMapOf<String, Any>()
        user["userId"] = userId
        user["name"] = name
        currentUserDB.updateChildren(user)
    }
}