package ru.devinside.drm.fairplay.ksm.spc;

public class SpcPayload {
    private final byte[] payload;

    public SpcPayload(byte[] payload) {
        this.payload = payload;
    }

    public byte[] getPayload() {
        return payload;
    }
}
