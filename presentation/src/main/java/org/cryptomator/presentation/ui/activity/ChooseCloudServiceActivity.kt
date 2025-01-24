package org.cryptomator.presentation.ui.activity

import android.view.MenuItem
import androidx.fragment.app.Fragment
import org.cryptomator.generator.Activity
import org.cryptomator.generator.InjectIntent
import org.cryptomator.presentation.R
import org.cryptomator.presentation.databinding.ActivityLayoutBinding
import org.cryptomator.presentation.intent.ChooseCloudServiceIntent
import org.cryptomator.presentation.intent.Intents.cloudSettingsIntent
import org.cryptomator.presentation.model.CloudTypeModel
import org.cryptomator.presentation.presenter.ChooseCloudServicePresenter
import org.cryptomator.presentation.ui.activity.view.ChooseCloudServiceView
import org.cryptomator.presentation.ui.fragment.ChooseCloudServiceFragment
import javax.inject.Inject

@Activity
class ChooseCloudServiceActivity : BaseActivity<ActivityLayoutBinding>(ActivityLayoutBinding::inflate), ChooseCloudServiceView {

	@Inject
	lateinit var presenter: ChooseCloudServicePresenter

	@InjectIntent
	lateinit var chooseCloudServiceIntent: ChooseCloudServiceIntent

	override fun setupView() {
		binding.mtToolbar.toolbar.setTitle(R.string.screen_choose_cloud_service_title)
		binding.mtToolbar.toolbar.subtitle = chooseCloudServiceIntent.subtitle()
		setSupportActionBar(binding.mtToolbar.toolbar)
	}

	override fun createFragment(): Fragment = ChooseCloudServiceFragment()

	override fun getCustomMenuResource(): Int = R.menu.menu_cloud_services

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		presenter.startIntent(cloudSettingsIntent())
		return super.onOptionsItemSelected(item)
	}

	override fun render(cloudModels: List<CloudTypeModel>) {
		chooseCloudServiceFragment().render(cloudModels)
	}

	private fun chooseCloudServiceFragment(): ChooseCloudServiceFragment = getCurrentFragment(R.id.fragment_container) as ChooseCloudServiceFragment
}
