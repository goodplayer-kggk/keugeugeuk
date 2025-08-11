package com.goodplayer.keugeugeuk

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.goodplayer.keugeugeuk.databinding.FragmentScratchBinding
import kotlinx.coroutines.*

class ScratchFragment : Fragment() {
    private var _binding: FragmentScratchBinding? = null
    private val binding get() = _binding!!
    private val pm by lazy { PointsManager(requireContext()) }
    private val scope = CoroutineScope(Dispatchers.Main + Job())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentScratchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.tvHint.text = "광고 시청 후 긁기 가능합니다."
        binding.btnWatchAd.setOnClickListener {
            // Simulate ad
            binding.progress.visibility = View.VISIBLE
            binding.btnWatchAd.isEnabled = false
            scope.launch {
                delay(1200) // simulate ad
                val reward = RewardGenerator.generateReward() // 2..10
                pm.addPoints(reward)
                binding.progress.visibility = View.GONE
                binding.btnWatchAd.isEnabled = true
                Toast.makeText(requireContext(), "광고 완료! +${reward}P 획득", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        scope.cancel()
        _binding = null
    }
}