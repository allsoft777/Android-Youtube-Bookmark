package com.owllife.youtubebookmark.presentation.player

import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Build
import android.widget.ImageView
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.fragment.app.FragmentActivity
import com.owllife.youtubebookmark.R
import com.owllife.youtubebookmark.domain.entity.BookMarkEntity
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener
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

fun initFullScreenListener(
    youtubePlayerView: YouTubePlayerView, activity: FragmentActivity
) {
    youtubePlayerView.addFullScreenListener(object :
        YouTubePlayerFullScreenListener {
        override fun onYouTubePlayerEnterFullScreen() {
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            youtubePlayerView.enterFullScreen()
        }

        override fun onYouTubePlayerExitFullScreen() {
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            youtubePlayerView.exitFullScreen()
        }
    })
}

fun initPipMode(youtubePlayerView: YouTubePlayerView, activity: FragmentActivity) {
    val pictureInPictureIcon = ImageView(youtubePlayerView.context)
    pictureInPictureIcon.setImageDrawable(
        ContextCompat.getDrawable(youtubePlayerView.context, R.drawable.ic_pip_24dp)
    )

    pictureInPictureIcon.setOnClickListener {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val supportsPIP: Boolean =
                activity.packageManager.hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE)
            if (supportsPIP)
                activity.enterPictureInPictureMode()
        } else {
            AlertDialog.Builder(activity)
                .setTitle("Can't enter picture in picture mode")
                .setMessage("In order to enter picture in picture mode you need a SDK version >= N.")
                .show()
        }
    }
    youtubePlayerView.getPlayerUiController().addView(pictureInPictureIcon)
}