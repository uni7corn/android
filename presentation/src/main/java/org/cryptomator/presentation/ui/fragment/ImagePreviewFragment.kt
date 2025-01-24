package org.cryptomator.presentation.ui.fragment

import android.os.Bundle
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import org.cryptomator.presentation.databinding.FragmentImagePreviewBinding
import org.cryptomator.presentation.di.HasComponent
import org.cryptomator.presentation.di.component.ActivityComponent
import org.cryptomator.presentation.model.ImagePreviewFile
import org.cryptomator.presentation.presenter.ImagePreviewPresenter
import javax.inject.Inject

class ImagePreviewFragment : Fragment() {

	@Inject
	lateinit var presenter: ImagePreviewPresenter

	private lateinit var binding: FragmentImagePreviewBinding

	private var imagePreviewFile: ImagePreviewFile? = null

	private var created: Boolean = false
	private var onViewCreatedCalled: Boolean = false

	private val clickDetector = GestureDetector(activity, object : GestureDetector.SimpleOnGestureListener() {

		override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
			presenter.onImagePreviewClicked()
			return true
		}
	})

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
		binding = FragmentImagePreviewBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		onViewCreatedCalled = true
	}

	override fun onStart() {
		super.onStart()

		if (!created) {
			((activity as HasComponent<*>).component as ActivityComponent)
				.inject(this)
		}

		if (onViewCreatedCalled) {
			setupView()
		}

		created = true
		onViewCreatedCalled = false
	}

	fun setupView() {
		imagePreviewFile = requireArguments().getParcelable(ARG_IMAGE_PREVIEW_FILE)
		imagePreviewFile?.let { imagePreviewFile ->
			imagePreviewFile.uri?.let {
				hideProgressBar()
				binding.imageView.orientation = SubsamplingScaleImageView.ORIENTATION_USE_EXIF
				showImage(imagePreviewFile)
			} ?: presenter.onMissingImagePreviewFile(imagePreviewFile)
		}

		binding.imageView.setOnTouchListener { _, event -> clickDetector.onTouchEvent(event) }
	}

	fun hideProgressBar() {
		binding.progressBar.visibility = View.GONE
	}

	private fun showImage(imagePreviewFile: ImagePreviewFile?) {
		binding.imageView.let { imageView ->
			imagePreviewFile?.let { imagePreviewFile ->
				imageView.orientation = SubsamplingScaleImageView.ORIENTATION_USE_EXIF
				imagePreviewFile.uri?.let { imageView.setImage(ImageSource.uri(it)) }
			}
		}
	}

	fun showAndUpdateImage(imagePreviewFile: ImagePreviewFile?) {
		this.imagePreviewFile = imagePreviewFile
		showImage(imagePreviewFile)
	}

	fun imagePreviewFile(): ImagePreviewFile? = imagePreviewFile

	companion object {

		private const val ARG_IMAGE_PREVIEW_FILE = "previewFile"

		fun newInstance(imagePreviewFile: ImagePreviewFile): ImagePreviewFragment {
			val args = Bundle()
			args.putParcelable(ARG_IMAGE_PREVIEW_FILE, imagePreviewFile)
			val fragment = ImagePreviewFragment()
			fragment.arguments = args
			return fragment
		}
	}

}
