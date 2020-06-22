package com.owllife.youtubebookmark.presentation.player

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
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
    private lateinit var youtubePlayerViewManager: YoutubePlayerViewManager
    private var viewModel: YoutubePlayerViewModel? = null

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_youtube_player)
        dataBinding.lifecycleOwner = this
        dataBinding.viewModel = viewModel
        viewModel?.loadData(intent)

        lifecycle.addObserver(dataBinding.youtubePlayerView)
        youtubePlayerViewManager = YoutubePlayerViewManager(dataBinding.youtubePlayerView, this)
        youtubePlayerViewManager.initUiButtons()
        youtubePlayerViewManager.initFullScreenListener()
        youtubePlayerViewManager.bindPipLiveData(this, getViewModel().isPipMode)
        youtubePlayerViewManager.bindUiControllerVisibility(
            this, getViewModel().showUiController
        )
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
        getViewModel().setPipMode(false)
        super.onBackPressed()
    }

    override fun onPause() {
        getViewModel().setPipMode(true)
        super.onPause()
    }

    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean,
        newConfig: Configuration?
    ) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        getViewModel().setShowUiController(!isInPictureInPictureMode)
        if (!isInPictureInPictureMode) {
            viewModel!!.setPipMode(false)
        }
    }

    private fun getViewModel(): YoutubePlayerViewModel {
        return dataBinding.viewModel!!
    }
}