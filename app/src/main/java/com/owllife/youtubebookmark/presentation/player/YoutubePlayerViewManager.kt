package com.owllife.youtubebookmark.presentation.player

import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Build
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.owllife.youtubebookmark.R
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

/**
 * @author owllife.dev
 * @since 20. 6. 22
 */
class YoutubePlayerViewManager(
    private val youTubePlayerView: YouTubePlayerView,
    private val activity: FragmentActivity
) {

    private var pictureInPictureIcon: ImageView = ImageView(activity)
    private var youTubePlayerFullScreenListener: YouTubePlayerFullScreenListener =
        YouTubePlayerFullScreenListenerImpl()

    init {
        activity.lifecycle.addObserver(youTubePlayerView)
        pictureInPictureIcon.setImageDrawable(
            ContextCompat.getDrawable(activity, R.drawable.ic_pip_24dp)
        )
    }

    @Suppress("DEPRECATION")
    fun initUiButtons() {
        if (!supportPipMode()) {
            return
        }
        pictureInPictureIcon.setOnClickListener {
            val hasFeature =
                activity.packageManager.hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE)
            if (hasFeature) activity.enterPictureInPictureMode()
        }
    }

    @Suppress("DEPRECATION")
    fun bindPipLiveData(lifecycleOwner: LifecycleOwner, liveData: LiveData<Boolean>) {
        if (!supportPipMode()) {
            return
        }
        liveData.observe(lifecycleOwner, Observer {
            if (it) {
                activity.enterPictureInPictureMode()
                youTubePlayerView.getPlayerUiController().addView(pictureInPictureIcon)
            } else {
                youTubePlayerView.getPlayerUiController().removeView(pictureInPictureIcon)
            }
        })
    }

    fun removeObservers(lifecycleOwner: LifecycleOwner, liveData: LiveData<Boolean>) {
        liveData.removeObservers(lifecycleOwner)
    }

    fun initFullScreenListener() {
        youTubePlayerView.addFullScreenListener(youTubePlayerFullScreenListener)
    }

    fun removeFullScreenListener() {
        youTubePlayerView.removeFullScreenListener(youTubePlayerFullScreenListener)
    }

    fun bindUiControllerVisibility(lifecycleOwner: LifecycleOwner, liveData: LiveData<Boolean>) {
        liveData.observe(lifecycleOwner, Observer {
            youTubePlayerView.getPlayerUiController().showUi(it)
        })
    }

    private fun supportPipMode(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
    }

    inner class YouTubePlayerFullScreenListenerImpl : YouTubePlayerFullScreenListener {

        override fun onYouTubePlayerEnterFullScreen() {
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            youTubePlayerView.enterFullScreen()
        }

        override fun onYouTubePlayerExitFullScreen() {
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            youTubePlayerView.exitFullScreen()
        }
    }
}