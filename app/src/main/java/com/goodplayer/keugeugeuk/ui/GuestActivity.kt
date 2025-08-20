package com.goodplayer.keugeugeuk.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.goodplayer.keugeugeuk.R
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class GuestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guest)

        val youTubePlayerView = findViewById<YouTubePlayerView>(R.id.youtubePlayerView)
        lifecycle.addObserver(youTubePlayerView)

        // 게스트 모드 영상 후보 리스트
        val videoIds = listOf(
            "dQw4w9WgXcQ", // 예시
            "M7lc1UVf-VE", // 예시
            "kxopViU98Xo", // 예시
            "mEnnz-yZ8MA"  // 예시
        )

        // 실행할 때마다 랜덤 선택
        val selectedVideoId = videoIds.random()

        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                youTubePlayer.loadVideo(selectedVideoId, 0f)
            }
        })
    }
}