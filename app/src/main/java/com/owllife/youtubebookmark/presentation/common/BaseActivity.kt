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

    var loadingDialog: Lazy<LoadingDialogView> = lazy { LoadingDialogView(this) }

    abstract fun getBaseViewModel(): BaseViewModel?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindViewModel()
    }

    private fun bindViewModel() {
        val viewModel = getBaseViewModel()
        viewModel?.let { vm ->
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

    override fun onDestroy() {
        loadingDialog.value.dismiss()
        super.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(0, 0)
    }
}