package ru.devinside.drm.fairplay.ksm.spc;

/**
 * The key for the AES decryption of the SPC payload, called SPCK.
 */
public class SpcKey {
    private final byte[] spck;

    public SpcKey(byte[] spck) {
        this.spck = spck;
    }

    public byte[] getSpck() {
        return spck;
    }
}
