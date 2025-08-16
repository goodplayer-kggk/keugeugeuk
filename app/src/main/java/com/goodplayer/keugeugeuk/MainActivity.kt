package com.goodplayer.keugeugeuk

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.goodplayer.keugeugeuk.ui.RewardsFragment
import com.goodplayer.keugeugeuk.ui.scratch.ScratchFragment
import com.goodplayer.keugeugeuk.databinding.ActivityMainBinding
import com.goodplayer.keugeugeuk.ui.exchange.ExchangeFragment
import com.goodplayer.keugeugeuk.ui.HomeFragment
import com.goodplayer.keugeugeuk.ui.lifetip.LifeTipsFragment
import com.google.android.gms.ads.MobileAds

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MobileAds.initialize(this) {
            Log.d("MainActivity", "AdMob initialized")
        }

        // 초기 프래그먼트: Home
        if (savedInstanceState == null) {
            replaceFragment(HomeFragment())
        }

        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> replaceFragment(HomeFragment())
                R.id.nav_scratch -> replaceFragment(ScratchFragment())
                R.id.nav_rewards -> replaceFragment(RewardsFragment())
                R.id.nav_exchange -> replaceFragment(ExchangeFragment())
                R.id.nav_tips -> replaceFragment(LifeTipsFragment())
            }
            true
        }
    }

    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.container.id, fragment)
            .commit()
    }
}