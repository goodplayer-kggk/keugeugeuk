package com.goodplayer.keugeugeuk.ui.settings

sealed class SettingItem {
    data class Normal(val icon: Int, val title: String) : SettingItem()
    data class Switch(val icon: Int, val title: String, var isChecked: Boolean) : SettingItem()
    object Divider : SettingItem()
    data class Header(val title: String) : SettingItem()
}