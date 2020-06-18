package com.owllife.youtubebookmark.presentation.player

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
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

    private var TAG = YoutubePlayerActivity::class.java.simpleName
    private lateinit var dataBinding: ActivityYoutubePlayerBinding
    private var viewModel: YoutubePlayerViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_youtube_player)
        dataBinding.lifecycleOwner = this
        dataBinding.viewModel = viewModel
        viewModel?.loadData(intent)
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

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onBackPressed() {
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } else {
            super.onBackPressed()
        }
    }
}