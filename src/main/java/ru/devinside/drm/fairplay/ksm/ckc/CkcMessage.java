package ru.devinside.drm.fairplay.ksm.ckc;

public class CkcMessage {
    private final CkcDataIv iv;
    private final CkcEncryptedPayload payload;

    public CkcMessage(CkcDataIv iv, CkcEncryptedPayload payload) {
        this.iv = iv;
        this.payload = payload;
    }

    public int getVersion() {
        return 0x00000001;
    }

    public int getReserved() {
        return 0x00000000;
    }

    public CkcDataIv getIv() {
        return iv;
    }

    public CkcEncryptedPayload getPayload() {
        return payload;
    }
}
