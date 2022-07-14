package com.example.airbnb

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class HouseListAdapter: ListAdapter<HouseModel, HouseListAdapter.ItemViewHolder>(differ) {
  inner class ItemViewHolder(val view: View): RecyclerView.ViewHolder(view) {
    fun bind(houseModel: HouseModel) {
      val titleTextView = view.findViewById<TextView>(R.id.titleTextView)
      val priceTextView = view.findViewById<TextView>(R.id.priceTextView)
      val thumbnailImageView = view.findViewById<ImageView>(R.id.thumbnailImageView)
      
      titleTextView.text = houseModel.title
      priceTextView.text = houseModel.price
      
      Glide.with(thumbnailImageView.context)
        .load(houseModel.imgUrl)
          // 이미지 변형
        // RoundedCorners의 경우 px값으로 들어가며 px은 모바일 기종마다 다르게 보일 수 있어서 dp값을 px로 변환해주는 함수가 필요하다.
        .transform(CenterCrop(), RoundedCorners(dpToPx(thumbnailImageView.context, 12)))
        .into(thumbnailImageView)
    }
  }
  
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
    val inflater = LayoutInflater.from(parent.context)
    return ItemViewHolder(inflater.inflate(R.layout.item_house, parent, false))
  }
  
  override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
    holder.bind(currentList[position])
  }
  
  // dp > px 변환 함수
  private fun dpToPx(context: Context, dp: Int): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), context.resources.displayMetrics).toInt()
  }
  
  companion object {
    val differ = object: DiffUtil.ItemCallback<HouseModel>() {
      override fun areItemsTheSame(oldItem: HouseModel, newItem: HouseModel): Boolean {
        return oldItem.id == newItem.id
      }
      
      override fun areContentsTheSame(oldItem: HouseModel, newItem: HouseModel): Boolean {
        return oldItem == newItem
      }
    }
  }
}