package com.goodplayer.keugeugeuk.ui.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.goodplayer.keugeugeuk.databinding.ItemSettingNormalBinding
import com.goodplayer.keugeugeuk.databinding.ItemSettingSwitchBinding
import com.goodplayer.keugeugeuk.databinding.ItemSettingHeaderBinding
import com.goodplayer.keugeugeuk.databinding.ItemSettingDividerBinding

class SettingAdapter(
    private val items: List<SettingItem>,
    private val onClick: (SettingItem) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_NORMAL = 0
        private const val TYPE_SWITCH = 1
        private const val TYPE_HEADER = 2
        private const val TYPE_DIVIDER = 3
    }

    override fun getItemViewType(position: Int): Int = when (items[position]) {
        is SettingItem.Normal -> TYPE_NORMAL
        is SettingItem.Switch -> TYPE_SWITCH
        is SettingItem.Header -> TYPE_HEADER
        is SettingItem.Divider -> TYPE_DIVIDER
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            TYPE_NORMAL -> {
                val binding = ItemSettingNormalBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                NormalViewHolder(binding)
            }
            TYPE_SWITCH -> {
                val binding = ItemSettingSwitchBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                SwitchViewHolder(binding)
            }
            TYPE_HEADER -> {
                val binding = ItemSettingHeaderBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                HeaderViewHolder(binding)
            }
            else -> {
                val binding = ItemSettingDividerBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                DividerViewHolder(binding)
            }
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        when(holder) {
            is NormalViewHolder -> {
                val normal = item as SettingItem.Normal
                holder.binding.icon.setImageResource(normal.icon)
                holder.binding.title.text = normal.title
                holder.itemView.setOnClickListener { onClick(item) }
            }
            is SwitchViewHolder -> {
                val sw = item as SettingItem.Switch
                holder.binding.icon.setImageResource(sw.icon)
                holder.binding.title.text = sw.title
                holder.binding.switchView.isChecked = sw.isChecked
                holder.binding.switchView.setOnCheckedChangeListener { _, isChecked ->
                    sw.isChecked = isChecked
                    onClick(item)
                }
            }
            is HeaderViewHolder -> {
                val header = item as SettingItem.Header
                holder.binding.title.text = header.title
            }
            is DividerViewHolder -> {
                // nothing
            }
        }
    }

    class NormalViewHolder(val binding: ItemSettingNormalBinding) : RecyclerView.ViewHolder(binding.root)
    class SwitchViewHolder(val binding: ItemSettingSwitchBinding) : RecyclerView.ViewHolder(binding.root)
    class HeaderViewHolder(val binding: ItemSettingHeaderBinding) : RecyclerView.ViewHolder(binding.root)
    class DividerViewHolder(val binding: ItemSettingDividerBinding) : RecyclerView.ViewHolder(binding.root)
}