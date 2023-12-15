package com.example.cryptoapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.Locale

class CryptoListAdapter(private val cryptoList: List<String>, private val onItemClick: (String, Int) -> Unit): RecyclerView.Adapter<CryptoViewHolder>(), Filterable {

    private var filteredList: List<String> = cryptoList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            CryptoViewHolder = CryptoViewHolder(LayoutInflater.from(parent.context)
        .inflate(R.layout.crypto_item_layout,parent,false))

    override fun getItemCount(): Int = filteredList.size

    override fun onBindViewHolder(holder: CryptoViewHolder, position: Int) {
        val currentItem = filteredList[position]
        holder.cryptoName.text = currentItem
        holder.itemView.setOnClickListener {
            onItemClick(currentItem, position)
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val query = constraint.toString().toLowerCase(Locale.ROOT).trim()
                filteredList = if (query.isEmpty()) {
                    cryptoList
                } else {
                    cryptoList.filter { it.toLowerCase(Locale.ROOT).contains(query) }
                }

                val results = FilterResults()
                results.values = filteredList
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredList = results?.values as List<String>
                notifyDataSetChanged()
            }
        }
    }
}

class CryptoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val cryptoName: TextView = itemView.findViewById(R.id.crypto_name)
}