package ru.devinside.drm.fairplay.ksm.spc;

import java.nio.ByteBuffer;

/**
 * Encrypted AES-128 key.
 * The key for decrypting the SPC payload.
 * This key is itself encrypted, using RSA public key encryption with Optimal Asymmetric Encryption Padding (OAEP).
 *
 * @see Spck
 */
public class SpckRaw {
    public final static int SPCK_RAW_SIZE = 128;

    private final byte[] spckEncrypted;

    public SpckRaw(byte[] spckEncrypted) {
        this.spckEncrypted = spckEncrypted;
    }

    public byte[] getSpckEncrypted() {
        return spckEncrypted;
    }

    public static SpckRaw from(ByteBuffer buffer) {
        byte[] spckEncrypted = new byte[SPCK_RAW_SIZE];
        buffer.get(spckEncrypted);
        return new SpckRaw(spckEncrypted);
    }
}
