package com.goodplayer.keugeugeuk.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.goodplayer.keugeugeuk.data.PointsManager
import com.goodplayer.keugeugeuk.databinding.FragmentRewardsBinding

class RewardsFragment : Fragment() {
    private var _binding: FragmentRewardsBinding? = null
    private val binding get() = _binding!!
    private val pm by lazy { PointsManager(requireContext()) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRewardsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.tvPoints.text = "${pm.getPoints()} P"
        val history = pm.getHistory()
        val list = history.map { "${if (it.amount>0) "+" else ""}${it.amount}P - ${it.reason}" }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, list)
        binding.lvHistory.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}