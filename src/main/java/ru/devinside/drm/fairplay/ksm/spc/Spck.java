package ru.devinside.drm.fairplay.ksm.spc;

/**
 * The key for the AES decryption of the SPC payload, called SPCK.
 */
public class Spck {
    private final byte[] spck;

    public Spck(byte[] spck) {
        this.spck = spck;
    }

    public byte[] getSpck() {
        return spck;
    }
}
