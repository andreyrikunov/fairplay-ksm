package ru.devinside.drm.fairplay.ksm.ckc;

/**
 * A 16-byte encryption of the content key provided by the server.
 */
public class CkcEncryptedCk {
    private final byte[] encryptedCk;

    public CkcEncryptedCk(byte[] encryptedCk) {
        this.encryptedCk = encryptedCk;
    }

    public byte[] getEncryptedCk() {
        return encryptedCk;
    }
}
