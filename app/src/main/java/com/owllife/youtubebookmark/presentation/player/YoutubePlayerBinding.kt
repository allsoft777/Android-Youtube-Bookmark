package com.owllife.youtubebookmark.presentation.player

import androidx.databinding.BindingAdapter
import com.owllife.youtubebookmark.domain.entity.BookMarkEntity
import kr.co.prnd.YouTubePlayerView

/**
 * @author owllife.dev
 * @since 20. 6. 16
 */
@BindingAdapter("play_video")
fun setVideoId(view: YouTubePlayerView, data: BookMarkEntity?) {
    data?.let { view.play(it.videoId) }
}