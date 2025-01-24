package org.cryptomator.presentation.ui.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import org.cryptomator.generator.Dialog
import org.cryptomator.presentation.R
import org.cryptomator.presentation.databinding.DialogAppUpdateBinding
import org.cryptomator.presentation.databinding.ViewDialogErrorBinding

@Dialog
class UpdateAppAvailableDialog : BaseProgressErrorDialog<UpdateAppAvailableDialog.Callback, DialogAppUpdateBinding>(DialogAppUpdateBinding::inflate) {

	interface Callback {

		fun installUpdate()
		fun cancelUpdateClicked()
		fun showUpdateWebsite()
	}

	public override fun setupDialog(builder: AlertDialog.Builder): android.app.Dialog {
		return builder //
			.setTitle(getString(R.string.dialog_update_available_title)) //
			.setPositiveButton(getString(R.string.dialog_update_available_update)) { _: DialogInterface, _: Int -> callback?.installUpdate() } //
			.setNeutralButton(getString(R.string.dialog_update_available_download_site)) { _: DialogInterface, _: Int -> callback?.showUpdateWebsite() }
			.setNegativeButton(getText(R.string.dialog_update_available_cancel)) { _: DialogInterface, _: Int -> callback?.cancelUpdateClicked() } //
			.setCancelable(false) //
			.create()
	}

	public override fun setupView() {
		val message = requireArguments().getSerializable(MESSAGE_ARG) as String
		binding.tvMessage.text = Html.fromHtml(message, Html.FROM_HTML_MODE_COMPACT)
	}

	override fun dialogProgressLayout(): LinearLayout {
		return binding.llDialogProgress.llProgress
	}

	override fun dialogProgressTextView(): TextView {
		return binding.llDialogProgress.tvProgress
	}

	override fun dialogErrorBinding(): ViewDialogErrorBinding {
		return binding.llDialogError
	}

	override fun enableViewAfterError(): View {
		return binding.tvMessage
	}

	companion object {

		private const val MESSAGE_ARG = "messageArg"
		fun newInstance(message: String): UpdateAppAvailableDialog {
			val dialog = UpdateAppAvailableDialog()
			val args = Bundle()
			args.putSerializable(MESSAGE_ARG, message)
			dialog.arguments = args
			return dialog
		}
	}
}
