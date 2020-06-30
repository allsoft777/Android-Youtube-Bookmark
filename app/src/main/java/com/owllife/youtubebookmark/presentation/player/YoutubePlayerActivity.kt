package com.owllife.youtubebookmark.presentation.player

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.owllife.youtubebookmark.R
import com.owllife.youtubebookmark.core.navToLauncherTask
import com.owllife.youtubebookmark.databinding.ActivityYoutubePlayerBinding
import com.owllife.youtubebookmark.injection.ViewModelFactory
import com.owllife.youtubebookmark.presentation.common.BaseActivity
import com.owllife.youtubebookmark.presentation.util.PresentationConstants

/**
 * @author owllife.dev
 * @since 20. 6. 11
 */
class YoutubePlayerActivity : BaseActivity() {

    private lateinit var dataBinding: ActivityYoutubePlayerBinding
    private lateinit var youtubePlayerViewManager: YoutubePlayerViewManager
    private val viewModel: YoutubePlayerViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelFactory(this, application)
        ).get(YoutubePlayerViewModel::class.java)
    }

    companion object {
        fun callingIntent(parentContext: Context, dbId: Int) = run {
            val intent = Intent(parentContext, YoutubePlayerActivity::class.java)
            intent.putExtra(PresentationConstants.KEY_DB_ID, dbId)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_youtube_player)
        dataBinding.lifecycleOwner = this
        dataBinding.viewModel = viewModel
        viewModel.loadData(intent)

        lifecycle.addObserver(dataBinding.youtubePlayerView)
        youtubePlayerViewManager = YoutubePlayerViewManager(dataBinding.youtubePlayerView, this)
        youtubePlayerViewManager.initUiButtons()
        youtubePlayerViewManager.initFullScreenListener()
        youtubePlayerViewManager.bindPipLiveData(this, viewModel.pipMode)
        youtubePlayerViewManager.bindUiControllerVisibility(
            this, viewModel.showUiController
        )
        viewModel.dataLoading.observe(this, Observer { isLoading ->
            if (isLoading) loadingDialog.value.show() else loadingDialog.value.dismiss()
        })
    }

    override fun onBackPressed() {
        if (dataBinding.youtubePlayerView.isFullScreen()) {
            dataBinding.youtubePlayerView.exitFullScreen()
            return
        }
        youtubePlayerViewManager.removeFullScreenListener()
        youtubePlayerViewManager.removeObservers(this, viewModel.pipMode)
        super.onBackPressed()
    }

    override fun onPause() {
        viewModel.setPipMode(true)
        super.onPause()
    }

    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean,
        newConfig: Configuration?
    ) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        viewModel.setShowUiController(!isInPictureInPictureMode)
        if (!isInPictureInPictureMode) {
            viewModel.setPipMode(false)
        }
    }

    override fun finish() {
        super.finish()
        navToLauncherTask()
    }
}