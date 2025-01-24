package org.cryptomator.presentation.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import org.cryptomator.generator.Activity
import org.cryptomator.presentation.R
import org.cryptomator.presentation.databinding.ActivityLayoutObscureAwareBinding
import org.cryptomator.presentation.intent.Intents.vaultListIntent
import org.cryptomator.presentation.presenter.LicenseCheckPresenter
import org.cryptomator.presentation.ui.activity.view.UpdateLicenseView
import org.cryptomator.presentation.ui.dialog.LicenseConfirmationDialog
import org.cryptomator.presentation.ui.dialog.UpdateLicenseDialog
import org.cryptomator.presentation.ui.layout.ObscuredAwareCoordinatorLayout
import javax.inject.Inject
import kotlin.system.exitProcess

@Activity
class LicenseCheckActivity : BaseActivity<ActivityLayoutObscureAwareBinding>(ActivityLayoutObscureAwareBinding::inflate), //
	UpdateLicenseDialog.Callback, //
	LicenseConfirmationDialog.Callback, //
	UpdateLicenseView {

	@Inject
	lateinit var licenseCheckPresenter: LicenseCheckPresenter

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding.activityRootView.setOnFilteredTouchEventForSecurityListener(object : ObscuredAwareCoordinatorLayout.Listener {
			override fun onFilteredTouchEventForSecurity() {
				licenseCheckPresenter.onFilteredTouchEventForSecurity()
			}
		})

		validate(intent)
	}

	override fun setupView() {
		setupToolbar()
	}

	override fun checkLicenseClicked(license: String?) {
		licenseCheckPresenter.validateDialogAware(license)
	}

	override fun onNewIntent(intent: Intent) {
		super.onNewIntent(intent)

		validate(intent)
	}

	fun validate(intent: Intent) {
		val data: Uri? = intent.data
		licenseCheckPresenter.validate(data)
	}

	override fun showOrUpdateLicenseDialog(license: String) {
		showDialog(UpdateLicenseDialog.newInstance(license))
	}

	override fun onCheckLicenseCanceled() {
		exitProcess(0)
	}

	override fun appObscuredClosingEnterLicenseDialog() {
		closeDialog()
		licenseCheckPresenter.onFilteredTouchEventForSecurity()
	}

	private fun setupToolbar() {
		binding.mtToolbar.toolbar.title = getString(R.string.app_name).uppercase()
		setSupportActionBar(binding.mtToolbar.toolbar)
	}

	override fun showConfirmationDialog(mail: String) {
		showDialog(LicenseConfirmationDialog.newInstance(mail))
	}

	override fun licenseConfirmationClicked() {
		vaultListIntent() //
			.preventGoingBackInHistory() //
			.startActivity(this) //
	}
}
