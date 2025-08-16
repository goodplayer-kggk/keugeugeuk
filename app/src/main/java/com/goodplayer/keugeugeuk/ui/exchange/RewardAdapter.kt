package com.goodplayer.keugeugeuk.ui.exchange

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.goodplayer.keugeugeuk.data.exchange.model.RewardItem
import com.goodplayer.keugeugeuk.databinding.ItemRewardBinding

class RewardAdapter(
    private val onClick: (RewardItem) -> Unit
) : ListAdapter<RewardItem, RewardAdapter.VH>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemRewardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding, onClick)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }

    class VH(
        private val binding: ItemRewardBinding,
        private val onClick: (RewardItem) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: RewardItem) = with(binding) {
            ivIcon.setImageResource(item.imageRes)
            tvName.text = item.name
            tvDesc.text = item.description
            tvCost.text = "${item.costPoints} P"
            btnExchange.setOnClickListener { onClick(item) }
        }
    }

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<RewardItem>() {
            override fun areItemsTheSame(old: RewardItem, new: RewardItem) = old.id == new.id
            override fun areContentsTheSame(old: RewardItem, new: RewardItem) = old == new
        }
    }
}