package ru.devinside.drm.fairplay.ksm.spc;

import java.nio.ByteBuffer;

/**
 * Encrypted AES-128 key. The key for decrypting the SPC payload.
 * This key is itself encrypted, using RSA public key encryption with Optimal Asymmetric Encryption Padding (OAEP).
 *
 * @see SpcKey
 */
@Deprecated
public class EncryptedSpcKey {
    public final static int ENCRYPTED_SPC_KEY_SIZE = 128;

    private final byte[] spckEncrypted;

    public EncryptedSpcKey(byte[] spckEncrypted) {
        this.spckEncrypted = spckEncrypted;
    }

    public byte[] getSpckEncrypted() {
        return spckEncrypted;
    }

    public static EncryptedSpcKey from(ByteBuffer buffer) {
        byte[] spckEncrypted = new byte[ENCRYPTED_SPC_KEY_SIZE];
        buffer.get(spckEncrypted);
        return new EncryptedSpcKey(spckEncrypted);
    }
}
