package com.goodplayer.keugeugeuk.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.goodplayer.keugeugeuk.data.PointsManager
import com.goodplayer.keugeugeuk.databinding.FragmentExchangeBinding

class ExchangeFragment : Fragment() {
    private var _binding: FragmentExchangeBinding? = null
    private val binding get() = _binding!!
    private val pm by lazy { PointsManager(requireContext()) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentExchangeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.tvMyPoints.text = "${pm.getPoints()} P"

        binding.btnExchange1.setOnClickListener {
            val cost = 500
            if (pm.consumePoints(cost, "Exchange: 스타벅스 아메리카노")) {
                Toast.makeText(
                    requireContext(),
                    "교환 신청 완료 (샘플). 관리자 확인 후 쿠폰 발송",
                    Toast.LENGTH_SHORT
                ).show()
                binding.tvMyPoints.text = "${pm.getPoints()} P"
            } else {
                Toast.makeText(requireContext(), "포인트 부족", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}