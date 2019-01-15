package ciphers.toyciphers

import ciphers.AesEcbCipher
import ciphers.Cipher
import utilities.base64ToBytes

/**
 * A cipher that encrypts the plaintext with AES in ECB mode with an unknown fixed key after suffixing unknown fixed
 * bytes.
 */

class ECBUnknownKeyUnknownPrefixUnknownSuffixCipher : Cipher {
    override var key: ByteArray = ByteArray(0)
        get() = throw IllegalStateException("The key of this cipher is fixed but unknown.")
    private val unknownKey = "FREEZE CONSTRUCT".toByteArray()
    private val unknownPrefix = ("Um9sbGluJyBpbiBteSA1LjAKV2l0aCBteSByYWctdG9wIGRvd24gc28gbXkgaGFpciBjYW4gYmx" +
            "vdwpUaGUgZ2lybGllcyBvbiBzdGFuZGJ5IHdhdmluZyBqdXN0IHRvIHNheSBoaQpEaWQgeW91IHN0b3A/IE5vLCBJIGp1c3QgZHJvdm" +
            "UgYnkK").base64ToBytes()
    private val unknownSuffix = ("Um9sbGluJyBpbiBteSA1LjAKV2l0aCBteSByYWctdG9wIGRvd24gc28gbXkgaGFpciBjYW4gYmx" +
            "vdwpUaGUgZ2lybGllcyBvbiBzdGFuZGJ5IHdhdmluZyBqdXN0IHRvIHNheSBoaQpEaWQgeW91IHN0b3A/IE5vLCBJIGp1c3QgZHJvdm" +
            "UgYnkK").base64ToBytes()
    private val cipher = AesEcbCipher()

    init {
        cipher.key = unknownKey
    }

    override fun encrypt(plaintext: ByteArray): ByteArray {
        return cipher.encrypt(unknownPrefix + plaintext + unknownSuffix)
    }

    override fun decrypt(ciphertext: ByteArray): ByteArray {
        val plaintextWithPrefixAndSuffix = cipher.decrypt(ciphertext)

        // TODO: Assuming it was correct to pad with null bytes in AES ECB.
        val firstPaddingByteIndex = if (0.toByte() in plaintextWithPrefixAndSuffix) {
            plaintextWithPrefixAndSuffix.indexOf(0.toByte())
        } else {
            plaintextWithPrefixAndSuffix.size
        }
        val firstSuffixByteIndex = firstPaddingByteIndex - unknownSuffix.size
        return plaintextWithPrefixAndSuffix.sliceArray(unknownPrefix.size until firstSuffixByteIndex)
    }
}