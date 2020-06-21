package com.owllife.youtubebookmark.presentation.player

import android.content.res.Configuration
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.owllife.youtubebookmark.R
import com.owllife.youtubebookmark.databinding.ActivityYoutubePlayerBinding
import com.owllife.youtubebookmark.injection.ViewModelFactory
import com.owllife.youtubebookmark.presentation.common.BaseActivity
import com.owllife.youtubebookmark.presentation.common.BaseViewModel

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
        initFullScreenListener(dataBinding.youtubePlayerView, this)
        initPipMode(dataBinding.youtubePlayerView, this)
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

    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean,
        newConfig: Configuration?
    ) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        if (isInPictureInPictureMode) {
            dataBinding.youtubePlayerView.enterFullScreen()
            dataBinding.youtubePlayerView.getPlayerUiController().showUi(false)
        } else {
            dataBinding.youtubePlayerView.exitFullScreen()
            dataBinding.youtubePlayerView.getPlayerUiController().showUi(true)
        }
    }
}