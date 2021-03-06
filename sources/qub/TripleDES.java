package qub;

/**
 * An encryption cipher that uses the DES cipher three times. See
 * https://en.wikipedia.org/wiki/Triple_DES for more information.
 */
public interface TripleDES
{
    /**
     * Encrypt the provided plaintext using DES and the provided initialization vector.
     * @param initializationVector The initialization vector (IV) to use to encrypt the provided
     *                             plaintext. This must be either 56 or 64 bits (7 or 8 bytes) long.
     *                             If 64 bits (8 bytes) are provided, then bits 8, 16, 24, 32, 40,
     *                             48, 56, and 64 are ignored.
     * @param plaintext The plaintext to encrypt. This block must be 64 bits (8 bytes) long.
     * @return The encrypted Block. This Block will be 64 bits (8 bytes) long.
     */
    static BitArray encrypt(BitArray initializationVector, BitArray plaintext)
    {
        PreCondition.assertNotNull(initializationVector, "initializationVector");
        PreCondition.assertOneOf(initializationVector.getCount(), new long[] { DES.blockSize * 2, DES.blockSize * 3 }, "initializationVector.getCount()");
        PreCondition.assertNotNull(plaintext, "plaintext");
        PreCondition.assertEqual(DES.blockSize, plaintext.getCount(), "plaintext.getCount()");

        final BitArray k1 = BitArray.create(DES.blockSize);
        k1.copyFrom(initializationVector, 0, 0, DES.blockSize);

        final BitArray k2 = BitArray.create(DES.blockSize);
        k2.copyFrom(initializationVector, DES.blockSize, 0, DES.blockSize);

        BitArray k3;
        if (initializationVector.getCount() == DES.blockSize * 2)
        {
            k3 = k1;
        }
        else
        {
            k3 = BitArray.create(DES.blockSize);
            k3.copyFrom(initializationVector, DES.blockSize * 2, 0, DES.blockSize);
        }

        final BitArray result = DES.encrypt(k3, DES.decrypt(k2, DES.encrypt(k1, plaintext)));

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(DES.blockSize, result.getCount(), "result.getCount()");

        return result;
    }

    /**
     * Decrypt the provided ciphertext using DES and the provided initialization vector.
     * @param initializationVector The initialization vector (IV) to use to decrypt the provided
     *                             ciphertext. This must be either 56 or 64 bits (7 or 8 bytes)
     *                             long. If 64 bits (8 bytes) are provided, then bits 8, 16, 24, 32,
     *                             40, 48, 56, and 64 are ignored.
     * @param ciphertext The ciphertext to encrypt. This block must be 64 bits (8 bytes) long.
     * @return The encrypted block. This block will be 64 bits (8 bytes) long.
     */
    static BitArray decrypt(BitArray initializationVector, BitArray ciphertext)
    {
        PreCondition.assertNotNull(initializationVector, "initializationVector");
        PreCondition.assertOneOf(initializationVector.getCount(), new long[] { DES.blockSize * 2, DES.blockSize * 3 }, "initializationVector.getCount()");
        PreCondition.assertNotNull(ciphertext, "ciphertext");
        PreCondition.assertEqual(DES.blockSize, ciphertext.getCount(), "ciphertext.getCount()");

        final BitArray k1 = BitArray.create(DES.blockSize);
        k1.copyFrom(initializationVector, 0, 0, DES.blockSize);

        final BitArray k2 = BitArray.create(DES.blockSize);
        k2.copyFrom(initializationVector, DES.blockSize, 0, DES.blockSize);

        BitArray k3;
        if (initializationVector.getCount() == DES.blockSize * 2)
        {
            k3 = k1;
        }
        else
        {
            k3 = BitArray.create(DES.blockSize);
            k3.copyFrom(initializationVector, DES.blockSize * 2, 0, DES.blockSize);
        }

        final BitArray result = DES.decrypt(k1, DES.encrypt(k2, DES.decrypt(k3, ciphertext)));

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(DES.blockSize, result.getCount(), "result.getCount()");

        return result;
    }
}
