package com.goodplayer.keugeugeuk.ui.history

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.goodplayer.keugeugeuk.databinding.ItemHistoryBinding
import com.goodplayer.keugeugeuk.auth.PointHistory
import java.text.SimpleDateFormat
import java.util.*

class HistoryAdapter(private var items: List<PointHistory>) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    class HistoryViewHolder(val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = items[position]

        holder.binding.tvReason.text = item.reason
        holder.binding.tvAmount.text = "${if (item.amount > 0) "+" else ""}${item.amount}P"
        holder.binding.tvAmount.setTextColor(
            if (item.amount > 0) 0xFF2E7D32.toInt() else 0xFFD32F2F.toInt()
        )

        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        holder.binding.tvDate.text = dateFormat.format(Date(item.timestamp))
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newItems: List<PointHistory>) {
        items = newItems
        notifyDataSetChanged()
    }
}