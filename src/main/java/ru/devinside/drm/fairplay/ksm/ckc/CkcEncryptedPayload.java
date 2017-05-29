package ru.devinside.drm.fairplay.ksm.ckc;

public class CkcEncryptedPayload {
    private final byte[] payload;

    public CkcEncryptedPayload(byte[] payload) {
        this.payload = payload;
    }

    public byte[] getPayload() {
        return payload;
    }
}
