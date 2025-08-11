package com.goodplayer.keugeugeuk

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.goodplayer.keugeugeuk.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val pm by lazy { PointsManager(requireContext()) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        updatePoints()
        binding.btnScratch.setOnClickListener {
            // 탭해서 긁기 화면으로 전환 — MainActivity에서 BottomNav를 통해 전환 가능, 또는 명시적 호출
            (activity as? MainActivity)?.replaceFragment(ScratchFragment()) // helper
        }
        binding.btnLifeTips.setOnClickListener {
            (activity as? MainActivity)?.replaceFragment(LifeTipsFragment())
        }
        binding.btnRewards.setOnClickListener {
            (activity as? MainActivity)?.replaceFragment(RewardsFragment())
        }
        binding.btnExchange.setOnClickListener {
            (activity as? MainActivity)?.replaceFragment(ExchangeFragment())
        }
    }

    private fun updatePoints() {
        binding.tvPoints.text = "${pm.getPoints()} P"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}