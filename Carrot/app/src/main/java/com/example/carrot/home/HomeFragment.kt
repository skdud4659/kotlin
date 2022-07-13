package com.example.carrot.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.carrot.DBKey.Companion.DB_ARTICLES
import com.example.carrot.R
import com.example.carrot.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class HomeFragment: Fragment(R.layout.fragment_home) {
  private lateinit var articleDB: DatabaseReference
  private lateinit var articleAdapter: ArticleAdapter
  
  private val articleList = mutableListOf<ArticleModel>()
  private val listener = object : ChildEventListener {
    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
  
      // model 전체를 전달
      val articleModel = snapshot.getValue(ArticleModel::class.java)
      Log.d("articleModel", "$articleModel")
      articleModel ?: return
      articleList.add(articleModel)
      articleAdapter.submitList(articleList)
    }
    override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
    override fun onChildRemoved(snapshot: DataSnapshot) {}
    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
    override fun onCancelled(error: DatabaseError) {}
  }
  
  private var binding: FragmentHomeBinding? = null
  private val auth: FirebaseAuth by lazy {
    Firebase.auth
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val fragmentHomeBinding = FragmentHomeBinding.bind(view)
    binding = fragmentHomeBinding
    
    Log.d("article", "$articleList")
    // clear를 하는 이유는 뷰가 재사용 되고있는중 articleList는 값을 계속 저장하고 있어
    // 값이 계속해서 쌓이기 때문에 clear를 선언하지 않으면 하나의 값이 여러번 나오는 경우가 발생하기 때문에 clear() 호출
    articleDB = Firebase.database.reference.child(DB_ARTICLES)
  
    articleAdapter = ArticleAdapter()
  
    // fragment는 context가 될 수 없기 때문에 getContext를 넣어준다. get은 생략 가능.
    fragmentHomeBinding.articleRecyclerView.layoutManager = LinearLayoutManager(context)
    fragmentHomeBinding.articleRecyclerView.adapter = articleAdapter
    
    // 한 번 등록 시 이벤트가 발생할때마다 등록됨. > fragment는 재사용되기 때문에 중복될 수 있기 때문에 onResume과 onDestroyView 때 삭제/실행해준다. > listener
    articleDB.addChildEventListener(listener)
  }
  
  @SuppressLint("NotifyDataSetChanged")
  override fun onResume() {
    super.onResume()
    articleAdapter.notifyDataSetChanged()
  }
  override fun onDestroyView() {
    super.onDestroyView()
    articleDB.removeEventListener(listener)
  }
}