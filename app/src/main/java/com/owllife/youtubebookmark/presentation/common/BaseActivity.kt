package com.owllife.youtubebookmark.presentation.common

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.owllife.youtubebookmark.core.showToastMsg
import com.owllife.youtubebookmark.presentation.data.FinishScreenData

/**
 * @author owllife.dev
 * @since 20. 6. 10
 */
abstract class BaseActivity : AppCompatActivity() {

    private lateinit var loadingView: LoadingDialogView

    abstract fun getBaseViewModel(): BaseViewModel?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadingView = LoadingDialogView(this)
        bindViewModel()
    }

    private fun bindViewModel() {
        val viewModel = getBaseViewModel()
        viewModel?.let { vm ->
//            vm.dataLoading.observe(this, Observer { show ->
//                if (show) loadingView.show() else loadingView.dismiss()
//            })
            vm.toastText.observe(this, Observer {
                showToastMsg(it)
            })
            vm.failureData.observe(this, Observer {
                it.exception.message?.let { msg -> showToastMsg(msg) }
            })
            vm.finishScreenData.observe(this, Observer {
                if (it is FinishScreenData.WithData) {
                    setResult(it.resultCode)
                }
                finish()
            })
        }
    }
}