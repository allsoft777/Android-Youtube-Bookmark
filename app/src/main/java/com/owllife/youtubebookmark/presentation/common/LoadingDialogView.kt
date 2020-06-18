package com.owllife.youtubebookmark.presentation.common

import android.content.Context
import android.content.DialogInterface
import android.view.KeyEvent
import android.view.LayoutInflater
import android.widget.MediaController
import android.widget.MediaController.MediaPlayerControl
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.owllife.youtubebookmark.R
import com.owllife.youtubebookmark.databinding.DialogLoadingBinding
import pl.droidsonroids.gif.GifDrawable
import pl.droidsonroids.gif.GifImageView
import java.io.IOException

/**
 * @author owllife.dev
 * @since 20. 6. 10
 */
class LoadingDialogView(private val parentContext: Context) {

    private var dialog: AlertDialog? = null

    fun show() {
        if (dialog == null) {
            create()
        }
        dialog!!.show()
    }

    fun dismiss() {
        dialog?.dismiss()
    }

    private fun create() {
        val builder = AlertDialog.Builder(parentContext, R.style.DialogStyle)
        builder.setCancelable(false)

        val dataBinding: DialogLoadingBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parentContext), R.layout.dialog_loading, null, false
        )

        val loadingView: GifImageView = dataBinding.loadingView
        loadingView.setFreezesAnimation(false)
        loadGifImage(loadingView)

        dialog =
            AlertDialog.Builder(parentContext, R.style.LoadingDialogTheme)
                .setView(dataBinding.root)
                .setCancelable(false)
                .setOnKeyListener { _: DialogInterface?, keyCode: Int, keyEvent: KeyEvent ->
                    keyCode == KeyEvent.KEYCODE_BACK && keyEvent.repeatCount == 0
                }
                .create()
    }

    private fun loadGifImage(
        imageView: GifImageView
    ) {
        try {
            val gifFromResource =
                GifDrawable(parentContext.resources, R.raw.loading_gradient_color)
            gifFromResource.setSpeed(1f)
            imageView.setImageDrawable(gifFromResource)
            val mc = MediaController(parentContext)
            mc.setMediaPlayer(imageView.drawable as MediaPlayerControl)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}