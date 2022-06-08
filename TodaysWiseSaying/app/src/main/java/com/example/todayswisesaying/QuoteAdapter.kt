package com.example.todayswisesaying

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class QuoteAdapter(
  private val quotes: List<Quote>,
  private val isNameRevealed: Boolean
): RecyclerView.Adapter<QuoteAdapter.QuoteViewHolder>() {
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
    QuoteViewHolder(
      LayoutInflater.from(parent.context)
        .inflate(R.layout.item_quote, parent, false)
    )
  
  override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
    val actualPosition = position % quotes.size
    holder.bind(quotes[actualPosition], isNameRevealed)
  }

  // 무한 스와이프
  override fun getItemCount() = Int.MAX_VALUE
  
  
  inner class QuoteViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val quoteText: TextView = itemView.findViewById(R.id.quote)
    private val nameText: TextView = itemView.findViewById(R.id.name)
    
    @SuppressLint("SetTextI18n")
    fun bind(quote: Quote, isNameRevealed: Boolean) {
      quoteText.text = "\"${quote.quote}\""
      if (isNameRevealed) {
        nameText.text = "- ${quote.name}"
        nameText.visibility = View.VISIBLE
      } else {
        nameText.visibility = View.GONE
      }
    }
  }
}