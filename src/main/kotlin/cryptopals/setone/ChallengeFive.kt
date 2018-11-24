package cryptopals.setone

import cryptopals.Challenge
import utilities.toHex
import kotlin.experimental.xor

object ChallengeFive: Challenge(1, 5) {
    override fun passes(): Boolean {
        val providedAsciiString = "Burning 'em, if you ain't quick and nimble\nI go crazy when I hear a cymbal"
        val providedKey = "ICE"
        val expectedHexString = "0b3637272a2b2e63622c2e69692a23693a2a3c6324202d623d63343c2a26226324272765272a282b2f20430a652e2c652a3124333a653e2b2027630c692b20283165286326302e27282f"

        val keyBytes = providedKey.toByteArray()
        val asciiStringBytes = providedAsciiString.toByteArray()

        val encryptedBytes = ByteArray(expectedHexString.length / 2)
        asciiStringBytes.forEachIndexed { idx, asciiStringByte ->
            val keyByte = keyBytes[idx % keyBytes.size]
            encryptedBytes[idx] = keyByte xor asciiStringByte
        }

        val encryptedHexString = encryptedBytes.toHex()

        return (encryptedHexString == expectedHexString)
    }
}