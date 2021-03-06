package challenges.setone

import challenges.Challenge
import utilities.hexToBytes
import java.io.File

/*
In this file are a bunch of hex-encoded ciphertexts.

One of them has been encrypted with ECB.

Detect it.

Remember that the problem with ECB is that it is stateless and deterministic; the same 16 byte plaintext block will
always produce the same 16 byte ciphertext.
*/
object ChallengeEight : Challenge(1, 8) {
    override fun passes(): Boolean {
        val providedCiphertexts = File("src/main/resources/challengedata/8.txt").readLines().map { it.hexToBytes() }
        val expectedCiphertext = ("d880619740a8a19b7840a8a31c810a3d08649af70dc06f4fd5d2d69c744cd283e2dd052f6b641dbf9" +
                "d11b0348542bb5708649af70dc06f4fd5d2d69c744cd2839475c9dfdbc1d46597949d9c7e82bf5a08649af70dc06f4fd5d2" +
                "d69c744cd28397a93eab8d6aecd566489154789a6b0308649af70dc06f4fd5d2d69c744cd283d403180c98c8f6db1f2a3f9" +
                "c4040deb0ab51b29933f2c123c58386b06fba186a").hexToBytes()

        val ecbBlockSize = 16
        val totalBlocks = providedCiphertexts.first().size / ecbBlockSize

        var ecbEncryptedCiphertext = ByteArray(0)
        var maxRepeatingBlocks = 0
        providedCiphertexts.forEach { bytes ->
            var repeatingBlocks = 0
            // TODO: Using a set isn't very efficient due to the way byte array equality works. Working around it for
            // TODO: now using toString().
            val seenBlocks = mutableSetOf<String>()

            (0 until totalBlocks).forEach { blockIdx ->
                val block = bytes.slice((blockIdx * ecbBlockSize) until (blockIdx + 1) * ecbBlockSize).toString()
                if (block in seenBlocks) repeatingBlocks += 1
                else seenBlocks.add(block)
            }

            if (repeatingBlocks > maxRepeatingBlocks) {
                maxRepeatingBlocks = repeatingBlocks
                ecbEncryptedCiphertext = bytes
            }
        }

        return ecbEncryptedCiphertext.contentEquals(expectedCiphertext)
    }
}
