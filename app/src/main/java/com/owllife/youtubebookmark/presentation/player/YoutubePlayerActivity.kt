package com.owllife.youtubebookmark.presentation.player

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.owllife.youtubebookmark.R
import com.owllife.youtubebookmark.databinding.ActivityYoutubePlayerBinding
import com.owllife.youtubebookmark.injection.ViewModelFactory
import com.owllife.youtubebookmark.presentation.common.BaseActivity
import com.owllife.youtubebookmark.presentation.common.BaseViewModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener

/**
 * @author owllife.dev
 * @since 20. 6. 11
 */
class YoutubePlayerActivity : BaseActivity() {

    private lateinit var dataBinding: ActivityYoutubePlayerBinding
    private var viewModel: YoutubePlayerViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_youtube_player)
        dataBinding.lifecycleOwner = this
        dataBinding.viewModel = viewModel
        viewModel?.loadData(intent)

        lifecycle.addObserver(dataBinding.youtubePlayerView)

        addFullScreenListenerToPlayer()
    }

    override fun getBaseViewModel(): BaseViewModel? {
        if (viewModel == null) {
            viewModel = ViewModelProvider(
                this,
                ViewModelFactory(this, application)
            ).get(YoutubePlayerViewModel::class.java)
        }
        return viewModel
    }

    override fun onBackPressed() {
        if (dataBinding.youtubePlayerView.isFullScreen()) {
            dataBinding.youtubePlayerView.exitFullScreen()
            return
        }
        super.onBackPressed()
    }

    private fun addFullScreenListenerToPlayer() {
        dataBinding.youtubePlayerView.addFullScreenListener(object :
            YouTubePlayerFullScreenListener {
            override fun onYouTubePlayerEnterFullScreen() {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                dataBinding.youtubePlayerView.enterFullScreen()
            }

            override fun onYouTubePlayerExitFullScreen() {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                dataBinding.youtubePlayerView.exitFullScreen()
            }
        })
    }
}