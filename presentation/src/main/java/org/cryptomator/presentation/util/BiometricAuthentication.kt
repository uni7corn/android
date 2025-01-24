package org.cryptomator.presentation.util

import android.content.Context
import android.security.keystore.KeyPermanentlyInvalidatedException
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import org.cryptomator.domain.Vault
import org.cryptomator.presentation.R
import org.cryptomator.presentation.model.VaultModel
import org.cryptomator.util.crypto.BiometricAuthCryptor
import org.cryptomator.util.crypto.UnrecoverableStorageKeyException
import java.util.concurrent.Executor
import javax.crypto.BadPaddingException
import timber.log.Timber

class BiometricAuthentication(val callback: Callback, val context: Context, val cryptoMode: CryptoMode, private val useConfirmationInFaceUnlockAuth: Boolean) {

	interface Callback {

		fun onBiometricAuthenticated(vault: VaultModel)

		fun onBiometricAuthenticationFailed(vault: VaultModel)

		fun onBiometricKeyInvalidated(vault: VaultModel)

	}

	enum class CryptoMode {
		ENCRYPT, DECRYPT
	}

	companion object {

		private lateinit var executor: Executor
		private lateinit var biometricPrompt: BiometricPrompt
		private lateinit var promptInfo: BiometricPrompt.PromptInfo
	}

	private var userCanceledDueToAuthActivity = false

	fun startListening(fragment: Fragment, vaultModel: VaultModel) {
		Timber.tag("BiometricAuthentication").d("Show biometric auth prompt")

		val biometricAuthCryptor: BiometricAuthCryptor

		try {
			biometricAuthCryptor = BiometricAuthCryptor.getInstance(context, org.cryptomator.util.crypto.CryptoMode.GCM)
		} catch (e: UnrecoverableStorageKeyException) {
			return callback.onBiometricKeyInvalidated(vaultModel)
		}

		executor = ContextCompat.getMainExecutor(context)
		biometricPrompt = BiometricPrompt(fragment, executor,
			object : BiometricPrompt.AuthenticationCallback() {
				override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
					super.onAuthenticationError(errorCode, errString)

					Timber.tag("BiometricAuthentication").e(String.format("Authentication error: %s errorCode=%d", errString, errorCode))

					if (!userCanceledDueToAuthActivity) {
						callback.onBiometricAuthenticationFailed(vaultModel)
						Timber.tag("BiometricAuthentication").i("Biometric authentication canceled by cloud authentication, restart as soon as authentication finishes")
					}
				}

				override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
					super.onAuthenticationSucceeded(result)

					Timber.tag("BiometricAuthentication").d("Authentication finished successfully")

					val cipher = result.cryptoObject?.cipher

					try {
						val transformedPassword = if (cryptoMode == CryptoMode.ENCRYPT) {
							biometricAuthCryptor.encrypt(cipher, vaultModel.password)
						} else {
							biometricAuthCryptor.decrypt(cipher, vaultModel.password)
						}

						val vaultModelPasswordAware = VaultModel(
							Vault //
								.aCopyOf(vaultModel.toVault()) //
								.withSavedPassword(transformedPassword, org.cryptomator.util.crypto.CryptoMode.GCM) //
								.build()
						)

						callback.onBiometricAuthenticated(vaultModelPasswordAware)
					} catch (e: BadPaddingException) {
						Timber.tag("BiometricAuthentication").i(
							e,
							"Recover from BadPaddingException which can be thrown on some devices if the key in the keystore is invalidated e.g. due to a fingerprint added because of an upstream error in Android, see #400 for more info"
						)
						callback.onBiometricKeyInvalidated(vaultModel)
					}
				}

				override fun onAuthenticationFailed() {
					super.onAuthenticationFailed()
					Timber.tag("BiometricAuthentication").e("Authentication failed")
				}
			})

		promptInfo = BiometricPrompt.PromptInfo.Builder()
			.setTitle(context.getString(R.string.dialog_biometric_auth_title))
			.setSubtitle(context.getString(R.string.dialog_biometric_auth_message))
			.setNegativeButtonText(context.getString(R.string.dialog_biometric_auth_use_password))
			.setConfirmationRequired(useConfirmationInFaceUnlockAuth)
			.build()

		try {
			val cryptoCipher = if (cryptoMode == CryptoMode.ENCRYPT) {
				biometricAuthCryptor.encryptCipher
			} else {
				biometricAuthCryptor.getDecryptCipher(vaultModel.password)
			}

			biometricPrompt.authenticate(
				promptInfo,
				BiometricPrompt.CryptoObject(cryptoCipher)
			)
		} catch (e: KeyPermanentlyInvalidatedException) {
			callback.onBiometricKeyInvalidated(vaultModel)
		}
	}

	fun stopListening() {
		biometricPrompt.cancelAuthentication()
		userCanceledDueToAuthActivity = true
	}

	fun stoppedBiometricAuthDuringCloudAuthentication(): Boolean {
		return userCanceledDueToAuthActivity
	}
}
