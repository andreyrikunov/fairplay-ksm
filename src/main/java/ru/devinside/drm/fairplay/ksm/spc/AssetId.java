package ru.devinside.drm.fairplay.ksm.spc;

public class AssetId {
    private final byte[] value; // TODO: always UTF-8?

    public AssetId(byte[] value) {
        this.value = value;
    }

    public byte[] getBytes() {
        return value;
    }
}
