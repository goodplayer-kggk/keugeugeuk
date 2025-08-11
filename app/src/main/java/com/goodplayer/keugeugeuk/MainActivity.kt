package com.goodplayer.keugeugeuk

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.goodplayer.keugeugeuk.databinding.ActivityMainBinding
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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