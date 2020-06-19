package com.owllife.youtubebookmark.presentation.player

import androidx.annotation.NonNull
import androidx.databinding.BindingAdapter
import androidx.fragment.app.FragmentActivity
import com.owllife.youtubebookmark.domain.entity.BookMarkEntity
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

/**
 * @author owllife.dev
 * @since 20. 6. 16
 */
@BindingAdapter("play_video")
fun loadVideo(view: YouTubePlayerView, data: BookMarkEntity?) {
    if (data == null) {
        return
    }
    val activity = view.context as FragmentActivity
    activity.lifecycle.addObserver(view)
    view.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
        override fun onReady(@NonNull youTubePlayer: YouTubePlayer) {
            youTubePlayer.loadVideo(data.videoId, 0f)
        }
    })
}