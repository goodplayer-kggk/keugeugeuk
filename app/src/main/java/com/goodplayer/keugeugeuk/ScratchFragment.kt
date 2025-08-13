package com.goodplayer.keugeugeuk

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.goodplayer.keugeugeuk.databinding.FragmentScratchBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.AdError
import com.goodplayer.keugeugeuk.ScratchResultActivity

class ScratchFragment : Fragment() {

    private var _binding: FragmentScratchBinding? = null
    private val binding get() = _binding!!
    private val pm by lazy { PointsManager(requireContext()) }

    private var interstitialAd: InterstitialAd? = null
    private var isScratching = false
    private val scratchThreshold = 0.2f // 20% 이상 긁으면 광고 실행

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScratchBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadInterstitialAd()

        // 긁기 이벤트
        binding.scratchView.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    isScratching = true
                }
                MotionEvent.ACTION_MOVE -> {
                    if (isScratching) {
                        binding.scratchView.handleTouch(event)
                    }
                }
                MotionEvent.ACTION_UP -> {
                    isScratching = false
                    val scratchedPercent = binding.scratchView.getScratchedPercentage()
                    if (scratchedPercent >= scratchThreshold) {
                        val reward = RewardGenerator.generateReward() // 2..10
                        pm.addPoints(reward)
                        showAdOrGoToResult(reward)
                    }
                }
            }
            true
        }
    }

    private fun loadInterstitialAd() {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            requireContext(),
            "ca-app-pub-3940256099942544/1033173712", // 테스트 ID
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    interstitialAd = null
                }

                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                }
            }
        )
    }

    private fun showAdOrGoToResult(reward:Int) {
        if (interstitialAd != null) {
            interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    goToResult(reward)
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    goToResult(-1)
                }
            }
            interstitialAd?.show(requireActivity())
        } else {
            goToResult(-1)
        }
    }

    private fun goToResult(reward:Int) {
        val intent = Intent(requireContext(), ScratchResultActivity::class.java)
        if(reward > 1)
            intent.putExtra("result_message", reward.toString()+"P 받았습니다.")
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}