package org.cryptomator.presentation.ui.fragment

import androidx.recyclerview.widget.LinearLayoutManager
import org.cryptomator.generator.Fragment
import org.cryptomator.presentation.databinding.FragmentCloudSettingsBinding
import org.cryptomator.presentation.model.CloudModel
import org.cryptomator.presentation.presenter.CloudSettingsPresenter
import org.cryptomator.presentation.ui.adapter.CloudSettingsAdapter
import org.cryptomator.presentation.ui.adapter.CloudSettingsAdapter.OnItemClickListener
import javax.inject.Inject

@Fragment
class CloudSettingsFragment : BaseFragment<FragmentCloudSettingsBinding>(FragmentCloudSettingsBinding::inflate) {

	@Inject
	lateinit var cloudSettingsPresenter: CloudSettingsPresenter

	@Inject
	lateinit var cloudSettingsAdapter: CloudSettingsAdapter

	private val onItemClickListener = object : OnItemClickListener {
		override fun onCloudClicked(cloudModel: CloudModel) {
			cloudSettingsPresenter.onCloudClicked(cloudModel)
		}
	}

	override fun loadContent() {
		cloudSettingsPresenter.loadClouds()
	}

	override fun setupView() {
		setupRecyclerView()
	}

	private fun setupRecyclerView() {
		cloudSettingsAdapter.setCallback(onItemClickListener)
		binding.rvCloudSettings.recyclerView.layoutManager = LinearLayoutManager(context())
		binding.rvCloudSettings.recyclerView.adapter = cloudSettingsAdapter
		// smoother scrolling
		binding.rvCloudSettings.recyclerView.setHasFixedSize(true)
	}

	fun showClouds(cloudModels: List<CloudModel>?) {
		cloudSettingsAdapter.clear()
		cloudSettingsAdapter.addAll(cloudModels)
	}

	fun update(cloud: CloudModel?) {
		cloudSettingsAdapter.notifyCloudChanged(cloud)
	}
}
