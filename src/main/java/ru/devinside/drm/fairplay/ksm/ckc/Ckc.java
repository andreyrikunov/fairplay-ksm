package ru.devinside.drm.fairplay.ksm.ckc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

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
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // TODO: use ByteBuffer backed serializer
        try {
            new CkcMessageSerializer(this).serializeTo(out);
        } catch (IOException e) {
            // Never happens, but but better be paranoiac
            throw new RuntimeException(e);
        }

        return out.toByteArray();
    }
}
