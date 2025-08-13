package com.goodplayer.keugeugeuk

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.goodplayer.keugeugeuk.databinding.FragmentLifetipsBinding
import androidx.recyclerview.widget.LinearLayoutManager

class LifeTipsFragment : Fragment() {
    private var _binding: FragmentLifetipsBinding? = null
    private val binding get() = _binding!!

    private val items = listOf(
        LifeTip(0, "아침 스트레칭 5분", "간단한 동작으로 기상 후 활력 UP"),
        LifeTip(1, "냉장고 정리 꿀팁", "식품 보관법으로 식재료 오래쓰기"),
        LifeTip(2, "초간단 1인 식사 레시피", "한 그릇으로 든든한 레시피")
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLifetipsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.rvTips.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTips.adapter = LifeTipAdapter(items) { tip ->
            val intent = Intent(requireContext(), TipDetailActivity::class.java)
            intent.putExtra("tip_title", tip.title)
            intent.putExtra("tip_description", tip.summary)
            intent.putExtra("tip_image", R.drawable.ic_launcher_foreground) // ic_tip_image 샘플 이미지
//            intent.putExtra("tip_id", tip.id)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}