package org.cryptomator.presentation.ui.activity

import androidx.fragment.app.Fragment
import org.cryptomator.generator.Activity
import org.cryptomator.presentation.R
import org.cryptomator.presentation.databinding.ActivityLayoutBinding
import org.cryptomator.presentation.model.CloudFolderModel
import org.cryptomator.presentation.model.VaultModel
import org.cryptomator.presentation.presenter.AutoUploadChooseVaultPresenter
import org.cryptomator.presentation.ui.activity.view.AutoUploadChooseVaultView
import org.cryptomator.presentation.ui.dialog.NotEnoughVaultsDialog
import org.cryptomator.presentation.ui.fragment.AutoUploadChooseVaultFragment
import javax.inject.Inject

@Activity
class AutoUploadChooseVaultActivity : BaseActivity<ActivityLayoutBinding>(ActivityLayoutBinding::inflate), //
	AutoUploadChooseVaultView, //
	NotEnoughVaultsDialog.Callback {

	@Inject
	lateinit var presenter: AutoUploadChooseVaultPresenter

	override fun setupView() {
		setupToolbar()
	}

	private fun setupToolbar() {
		binding.mtToolbar.toolbar.setTitle(R.string.screen_settings_auto_photo_upload_title)
		setSupportActionBar(binding.mtToolbar.toolbar)
		supportActionBar?.let {
			it.setDisplayHomeAsUpEnabled(true)
			it.setHomeAsUpIndicator(R.drawable.ic_clear)
		}
	}

	override fun onMenuItemSelected(itemId: Int): Boolean = when (itemId) {
		android.R.id.home -> {
			// finish this activity and does not call the onCreate method of the parent activity
			finish()
			true
		}
		else -> super.onMenuItemSelected(itemId)
	}

	override fun createFragment(): Fragment = AutoUploadChooseVaultFragment()

	override fun displayVaults(vaults: List<VaultModel>) {
		autoUploadChooseVaultFragment().displayVaults(vaults)
	}

	override fun displayDialogUnableToUploadFiles() {
		NotEnoughVaultsDialog //
			.withContext(context()) //
			.andTitle(R.string.dialog_unable_to_auto_upload_files_title) //
			.show()
	}

	override fun onNotEnoughVaultsOkClicked() {
		finish()
	}

	override fun onNotEnoughVaultsCreateVaultClicked() {
		// FIXME #202: vault list activity is twice on the stack
		val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
		launchIntent?.let { startActivity(it) }
		finish()
	}

	override fun showChosenLocation(location: CloudFolderModel) {
		autoUploadChooseVaultFragment().showChosenLocation(location)
	}

	private fun autoUploadChooseVaultFragment(): AutoUploadChooseVaultFragment = getCurrentFragment(R.id.fragment_container) as AutoUploadChooseVaultFragment
}
