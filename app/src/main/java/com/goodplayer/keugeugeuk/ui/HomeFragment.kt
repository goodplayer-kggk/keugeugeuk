package com.goodplayer.keugeugeuk.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.goodplayer.keugeugeuk.R
import com.goodplayer.keugeugeuk.auth.UserManager
import com.goodplayer.keugeugeuk.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        updatePoints()
        binding.btnScratch.setOnClickListener {
            findNavController().navigate(R.id.scratchFragment)
        }
        binding.btnLifeTips.setOnClickListener {
            findNavController().navigate(R.id.lifeTipsFragment)
        }
        binding.btnRewards.setOnClickListener {
            findNavController().navigate(R.id.rewardsFragment)
        }
        binding.btnExchange.setOnClickListener {
            findNavController().navigate(R.id.exchangeFragment)
        }
    }

    private fun updatePoints() {
        binding.tvPoints.text = "${UserManager.getPoints()} P"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}