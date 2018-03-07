package ru.devinside.drm.fairplay.ksm.ckc;

import java.nio.ByteBuffer;

public class Ckc {
    private final CkcDataIv iv;
    private final CkcEncryptedPayload payload;

    public Ckc(CkcDataIv iv, CkcEncryptedPayload payload) {
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

    public byte[] getBytes() {
        byte[] payload = getPayload().getPayload();
        ByteBuffer buffer = ByteBuffer.allocate(4 + 4 + CkcDataIv.CPC_DATA_IV_SIZE + 4 + payload.length);
        buffer.putInt(getVersion());
        buffer.putInt(getReserved());
        buffer.put(getIv().getIv());
        buffer.putInt(payload.length);
        buffer.put(payload);
        return buffer.array();
    }
}
