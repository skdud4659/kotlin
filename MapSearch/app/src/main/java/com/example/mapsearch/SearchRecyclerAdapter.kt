package com.example.mapsearch

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mapsearch.databinding.ViewholderSearchResultItemBinding
import com.example.mapsearch.model.SearchModel

class SearchRecyclerAdapter: ListAdapter<SearchModel, SearchRecyclerAdapter.ViewHolder>(diffUtil) {
  inner class ViewHolder(val binding: ViewholderSearchResultItemBinding): RecyclerView.ViewHolder(binding.root) {
    fun bindData(data: SearchModel) = with(binding) {
      searchItemTitle.text = data.name
      searchItemSubTitle.text = data.fullAddress
    }
  
    fun bindView() {
      binding.root.setOnClickListener {
        Toast.makeText(binding.root.context, "클릭!!", Toast.LENGTH_SHORT).show()
      }
    }
  }
  
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ViewHolder(ViewholderSearchResultItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
  }
  
  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bindData(currentList[position])
    holder.bindView()
  }
  
  companion object {
    val diffUtil = object: DiffUtil.ItemCallback<SearchModel>() {
      override fun areItemsTheSame(oldItem: SearchModel, newItem: SearchModel): Boolean {
        return oldItem == newItem
      }
  
      override fun areContentsTheSame(oldItem: SearchModel, newItem: SearchModel): Boolean {
        return oldItem == newItem
      }
    }
  }
}