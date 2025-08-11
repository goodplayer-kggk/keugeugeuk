package com.goodplayer.keugeugeuk

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.goodplayer.keugeugeuk.databinding.ItemLifetipBinding

class LifeTipAdapter(private val items: List<LifeTip>, private val onClick: (LifeTip)->Unit) :
    RecyclerView.Adapter<LifeTipAdapter.VH>() {

    inner class VH(val binding: ItemLifetipBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: LifeTip) {
            binding.tvTitle.text = item.title
            binding.tvSummary.text = item.summary
            binding.root.setOnClickListener { onClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VH(ItemLifetipBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(items[position])
    override fun getItemCount() = items.size
}