package com.owllife.youtubebookmark.presentation.player

import androidx.annotation.NonNull
import androidx.databinding.BindingAdapter
import com.owllife.youtubebookmark.entity.BookMarkEntireVO
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

/**
 * @author owllife.dev
 * @since 20. 6. 16
 */
@BindingAdapter("add_player_listener")
fun addPlayerListener(view: YouTubePlayerView, data: BookMarkEntireVO?) {
    if (data == null) {
        return
    }
    view.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
        override fun onReady(@NonNull youTubePlayer: YouTubePlayer) {
            youTubePlayer.loadVideo(data.videoId, 0f)
        }
    })
}