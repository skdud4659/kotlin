package com.example.todayswisesaying

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class QuoteAdapter(
  private val quotes: List<Quote>
): RecyclerView.Adapter<QuoteAdapter.QuoteViewHolder>() {
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
    QuoteViewHolder(
      LayoutInflater.from(parent.context)
        .inflate(R.layout.item_quote, parent, false)
    )
  
  override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
    holder.bind(quotes[position])
  }
  
  override fun getItemCount() = quotes.size
  
  
  inner class QuoteViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val quoteText: TextView = itemView.findViewById(R.id.quote)
    private val nameText: TextView = itemView.findViewById(R.id.name)
    
    fun bind(quote: Quote) {
      quoteText.text = quote.quote
      nameText.text = quote.name
    }
  }
}