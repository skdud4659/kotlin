package com.example.carrot.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.carrot.R
import com.example.carrot.databinding.FragmentHomeBinding

class HomeFragment: Fragment(R.layout.fragment_home) {
  private lateinit var articleAdapter: ArticleAdapter
  private var binding: FragmentHomeBinding? = null
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val fragmentHomeBinding = FragmentHomeBinding.bind(view)
    binding = fragmentHomeBinding
  
    // fragment는 context가 될 수 없기 때문에 getContext를 넣어준다. get은 생략 가능.
    fragmentHomeBinding.articleRecyclerView.layoutManager = LinearLayoutManager(context)
  
    articleAdapter = ArticleAdapter()
    articleAdapter.submitList(mutableListOf<ArticleModel>().apply {
      add(ArticleModel("0", "aaa", 1000000, "5000원", ""))
      add(ArticleModel("0", "bbb", 2000000, "10000원", ""))
    })
    fragmentHomeBinding.articleRecyclerView.adapter = articleAdapter
  }
}