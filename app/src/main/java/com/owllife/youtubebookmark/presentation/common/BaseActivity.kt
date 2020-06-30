package com.owllife.youtubebookmark.presentation.common

import androidx.appcompat.app.AppCompatActivity

/**
 * @author owllife.dev
 * @since 20. 6. 10
 */
abstract class BaseActivity : AppCompatActivity() {

    var loadingDialog: Lazy<LoadingDialogView> = lazy { LoadingDialogView(this) }

    override fun onDestroy() {
        loadingDialog.value.dismiss()
        super.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(0, 0)
    }
}