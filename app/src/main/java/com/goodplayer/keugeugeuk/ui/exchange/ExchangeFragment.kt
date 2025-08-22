package com.goodplayer.keugeugeuk.ui.exchange

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.goodplayer.keugeugeuk.R
import com.goodplayer.keugeugeuk.data.exchange.model.RewardItem
import com.goodplayer.keugeugeuk.databinding.DialogExchangeResultBinding
import com.goodplayer.keugeugeuk.databinding.FragmentExchangeBinding

class ExchangeFragment : Fragment(R.layout.fragment_exchange) {

    private var _binding: FragmentExchangeBinding? = null
    private val binding get() = _binding!!

    private val vm: ExchangeViewModel by viewModels()
    private lateinit var adapter: RewardAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentExchangeBinding.bind(view)
        adapter = RewardAdapter(
            onClick = { item -> confirmAndExchange(item) }
        )

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        binding.swipeRefresh.setOnRefreshListener {
            vm.loadRewards()
        }

        vm.rewards.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            binding.swipeRefresh.isRefreshing = false
        }

        vm.points.observe(viewLifecycleOwner) { p ->
            binding.tvPoints.text = getString(R.string.exchange_points_fmt, p)
        }

        vm.loading.observe(viewLifecycleOwner) { loading ->
            binding.progress.visibility = if (loading) View.VISIBLE else View.GONE
        }

        vm.exchangeResult.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                showResultDialog(result.success, result.couponCode, result.message)
                vm.clearExchangeResult()
            }
        }

        vm.refreshPoints()
        vm.loadRewards()
    }

    private fun confirmAndExchange(item: RewardItem) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("교환 확인")
            .setMessage("${item.name}(${item.costPoints}P)로 교환하시겠어요?")
            .setNegativeButton("취소", null)
            .setPositiveButton("교환") { _, _ ->
                vm.exchange(item)
            }
            .show()
    }

    private fun showResultDialog(success: Boolean, code: String?, message: String) {
        val dialogView = DialogExchangeResultBinding.inflate(layoutInflater)
        dialogView.tvStatus.text = if (success) "교환 완료" else "교환 실패"
        dialogView.tvMessage.text = message
        dialogView.tvCode.apply {
            visibility = if (success && !code.isNullOrBlank()) View.VISIBLE else View.GONE
            text = code ?: ""
        }

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogView.root)
            .setPositiveButton("확인", null)
            .create()

        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}