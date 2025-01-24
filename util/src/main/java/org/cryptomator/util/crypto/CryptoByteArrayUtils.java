package org.cryptomator.util.crypto;

public class CryptoByteArrayUtils {

	public static byte[] getBytes(byte[] encryptedBytesWithIv, int ivLength) {
		if (encryptedBytesWithIv == null) {
			throw new IllegalArgumentException("Input array must not be null");
		}
		byte[] bytes = new byte[encryptedBytesWithIv.length - ivLength];
		System.arraycopy(encryptedBytesWithIv, ivLength, bytes, 0, bytes.length);
		return bytes;
	}

	public static byte[] join(byte[] encrypted, byte[] iv) {
		if (encrypted == null || iv == null) {
			throw new IllegalArgumentException("Input arrays must not be null");
		}
		byte[] result = new byte[iv.length + encrypted.length];
		System.arraycopy(iv, 0, result, 0, iv.length);
		System.arraycopy(encrypted, 0, result, iv.length, encrypted.length);
		return result;
	}
}
