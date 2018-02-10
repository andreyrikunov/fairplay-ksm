package ru.devinside.drm.fairplay.ksm.ckc;

/**
 * A 16-byte CBC initialization vector used in AES encryption and decryption  of audio and video assets
 */
public class ContentKeyIv {
    public final static int CONTENT_KEY_DATA_IV_SIZE = 16;

    private final byte[] iv;

    public ContentKeyIv(byte[] iv) {
        this.iv = iv;
    }

    public byte[] getIv() {
        return iv;
    }
}
