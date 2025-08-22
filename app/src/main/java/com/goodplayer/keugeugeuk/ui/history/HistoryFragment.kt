package com.goodplayer.keugeugeuk.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.goodplayer.keugeugeuk.auth.UserManager
import com.goodplayer.keugeugeuk.auth.PointHistory
import com.goodplayer.keugeugeuk.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: HistoryAdapter
    private var history = listOf<PointHistory>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // 메인 툴바 제목만 변경
//        (requireActivity() as AppCompatActivity).supportActionBar?.title = "포인트 내역 - " + UserManager.getNickname()
        binding.tvPoints.text = "${UserManager.getPoints()} P"

        history = UserManager.getHistory()

        adapter = HistoryAdapter(history)
        binding.rvHistory.adapter = adapter
        binding.rvHistory.layoutManager = LinearLayoutManager(requireContext())

        val filterOptions = listOf("전체", "획득", "교환")
        val spinnerAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, filterOptions)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spFilter.adapter = spinnerAdapter

        binding.spFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                updateHistoryList(filterOptions[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun updateHistoryList(filter: String) {
        val filtered = when (filter) {
            "획득" -> history.filter { it.amount > 0 }
            "교환" -> history.filter { it.amount < 0 }
            else -> history
        }
        adapter.updateData(filtered)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}