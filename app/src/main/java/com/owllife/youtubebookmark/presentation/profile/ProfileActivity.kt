package com.owllife.youtubebookmark.presentation.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.owllife.youtubebookmark.R
import com.owllife.youtubebookmark.core.configureDefaultToolbar
import com.owllife.youtubebookmark.databinding.ActivityMyProfileBinding
import com.owllife.youtubebookmark.injection.ViewModelFactory
import com.owllife.youtubebookmark.presentation.common.BaseActivity
import com.owllife.youtubebookmark.presentation.common.BaseViewModel
import com.owllife.youtubebookmark.presentation.login.LoginActivity
import kotlinx.android.synthetic.main.toolbar_title_only.*

/**
 * @author owllife.dev
 * @since 20. 6. 17
 */
class ProfileActivity : BaseActivity() {

    private lateinit var dataBinding: ActivityMyProfileBinding
    private var viewModel: ProfileViewModel? = null

    companion object {
        private const val REQ_CODE_SIGN_IN = 1

        fun callingIntent(parentContext: Context) = run {
            val intent = Intent(parentContext, ProfileActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_my_profile)
        dataBinding.lifecycleOwner = this
        dataBinding.viewModel = viewModel

        configureDefaultToolbar(toolbar, getString(R.string.profile))
        bindProfileData()
    }

    override fun getBaseViewModel(): BaseViewModel? {
        if (viewModel == null) {
            viewModel = ViewModelProvider(
                this,
                ViewModelFactory(this, application)
            ).get(ProfileViewModel::class.java)
        }
        return viewModel
    }

    private fun bindProfileData() {
        viewModel?.profileData?.observe(this, Observer {
            if (it == null || it.email.isNullOrEmpty()) {
                val intent = LoginActivity.callingIntent(this)
                startActivityForResult(intent, REQ_CODE_SIGN_IN)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == LoginActivity.REQ_CODE_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {
                viewModel?.loadProfile()
            } else {
                finish()
            }
            return
        }
        return super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onStart() {
        super.onStart()
        viewModel?.loadProfile()
    }
}