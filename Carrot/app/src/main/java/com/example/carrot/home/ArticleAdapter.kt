package com.example.carrot.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.carrot.databinding.ItemArticleBinding
import java.text.SimpleDateFormat
import java.util.*

class ArticleAdapter: ListAdapter<ArticleModel, ArticleAdapter.ViewHolder>(diffUtil) {
  inner class ViewHolder(private val binding: ItemArticleBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(articleModel: ArticleModel) {
      val format = SimpleDateFormat("MM월 dd일")
      val date = Date(articleModel.createAt)
      binding.titleTextView.text = articleModel.title
      binding.dateTextView.text = format.format(date).toString()
      binding.priceTextView.text = articleModel.price
  
      if (articleModel.imageUrl.isNotEmpty()) {
        Glide.with(binding.thumbnailImageView)
          .load(articleModel.imageUrl)
          .into(binding.thumbnailImageView)
      }
    }
  }
  
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ViewHolder(ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false))
  }
  
  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bind(currentList[position])
  }
  companion object {
    val diffUtil = object : DiffUtil.ItemCallback<ArticleModel>() {
      // 현재 노출되고 있는 아이템과 새로운 아이템이 같은지 비교해서 새로운 값이 들어왔을 때 호출
      override fun areItemsTheSame(oldItem: ArticleModel, newItem: ArticleModel): Boolean {
        return oldItem.createAt == newItem.createAt
      }
  
      // 현재 노출되고 있는 아이템과 새로운 아이템이 동일한가?
      override fun areContentsTheSame(oldItem: ArticleModel, newItem: ArticleModel): Boolean {
        return oldItem == newItem
      }
    }
  }
}