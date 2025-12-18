package com.dcook.weighttrackingapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WeightAdapter(
    private var items: MutableList<AppDatabaseHelper.WeightEntry>,
    private val onDeleteClicked: (AppDatabaseHelper.WeightEntry) -> Unit
) : RecyclerView.Adapter<WeightAdapter.WeightViewHolder>() {

    class WeightViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDate: TextView = itemView.findViewById(R.id.tvRowDate)
        val tvWeight: TextView = itemView.findViewById(R.id.tvRowWeight)
        val btnDelete: Button = itemView.findViewById(R.id.btnRowDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeightViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_weight, parent, false)
        return WeightViewHolder(view)
    }

    override fun onBindViewHolder(holder: WeightViewHolder, position: Int) {
        val entry = items[position]
        holder.tvDate.text = entry.date
        holder.tvWeight.text = "${entry.weight} lbs"
        holder.btnDelete.setOnClickListener { onDeleteClicked(entry) }
    }

    override fun getItemCount(): Int = items.size

    fun updateData(newItems: List<AppDatabaseHelper.WeightEntry>) {
        items = newItems.toMutableList()
        notifyDataSetChanged()
    }
}