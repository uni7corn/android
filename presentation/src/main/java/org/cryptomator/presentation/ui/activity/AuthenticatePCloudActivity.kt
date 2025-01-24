package org.cryptomator.presentation.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.widget.Toast
import org.cryptomator.generator.Activity
import org.cryptomator.presentation.BuildConfig
import org.cryptomator.presentation.R
import org.cryptomator.presentation.databinding.ActivityLayoutBinding
import org.cryptomator.presentation.presenter.CloudConnectionListPresenter
import java.util.TreeMap
import timber.log.Timber

@Activity
class AuthenticatePCloudActivity : BaseActivity<ActivityLayoutBinding>(ActivityLayoutBinding::inflate) {

	private val startAuthenticationRequestCode = 1232
	private val redirectTimeoutAfterAuthenticationAndResumed = 1000L

	private var cancelAuthenticationHandler: Handler = Handler()
	private var oAuthResultReceived = false

	override fun setupView() {
		val uri = Uri.parse("https://my.pcloud.com/oauth2/authorize")
			.buildUpon()
			.appendQueryParameter("response_type", "token")
			.appendQueryParameter("client_id", BuildConfig.PCLOUD_CLIENT_ID)
			.appendQueryParameter("redirect_uri", "pcloudoauth://redirect")
			.build()

		startActivityForResult(Intent(Intent.ACTION_VIEW, uri), startAuthenticationRequestCode)
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
		super.onActivityResult(requestCode, resultCode, intent)
		if (requestCode == startAuthenticationRequestCode) {
			cancelAuthenticationHandler.postDelayed({
				if (!oAuthResultReceived) {
					Timber.tag("AuthenticatePCloudActivity").i("Authentication canceled or no redirect received after resuming Cryptomator since 1.5s")
					Toast.makeText(context(), R.string.error_authentication_failed, Toast.LENGTH_SHORT).show()
					finish()
				}
			}, redirectTimeoutAfterAuthenticationAndResumed)
		}
	}

	override fun onNewIntent(intent: Intent) {
		super.onNewIntent(intent)
		intent.data?.let {
			if (it.host == "redirect" && it.scheme == "pcloudoauth") {
				oAuthResultReceived = true
				val parameters = parseUrlFragmentParameters(it)
				val accessToken = parameters["access_token"]
				val hostname = parameters["hostname"]
				if (accessToken != null && hostname != null) {
					val result = Intent()
					result.putExtra(CloudConnectionListPresenter.PCLOUD_OAUTH_AUTH_CODE, accessToken)
					result.putExtra(CloudConnectionListPresenter.PCLOUD_HOSTNAME, hostname)
					setResult(android.app.Activity.RESULT_OK, result)
				} else {
					Toast.makeText(this, R.string.error_authentication_failed, Toast.LENGTH_LONG).show()
					Timber.tag("AuthenticatePCloudActivity").i("Authentication failed as the access token or hostname is null")
				}
				finish()
			} else {
				Timber.tag("AuthenticatePCloudActivity").e("Tried to call activity using a different redirect scheme")
			}
		}
	}

	private fun parseUrlFragmentParameters(url: Uri): Map<String, String> {
		url.fragment?.let {
			val parameters: MutableMap<String, String> = TreeMap()
			val keyPairs = it.split("&".toRegex()).toTypedArray()
			keyPairs.forEach { keyPair ->
				val delimiterIndex = keyPair.indexOf('=')
				parameters[keyPair.substring(0, delimiterIndex)] = keyPair.substring(delimiterIndex + 1, keyPair.length)
			}
			return parameters
		}
		return emptyMap()
	}

	override fun onDestroy() {
		super.onDestroy()
		cancelAuthenticationHandler.removeCallbacksAndMessages(null)
	}
}
