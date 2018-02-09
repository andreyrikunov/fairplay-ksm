package ru.devinside.drm.fairplay.ksm.ckc;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class CkcMessageSerializer {
    private final Ckc ckc;

    public CkcMessageSerializer(Ckc ckc) {
        this.ckc = ckc;
    }

    public void serializeTo(OutputStream out) throws IOException {
        out.write(ByteBuffer.allocate(4).putInt(ckc.getVersion()).array());
        out.write(ByteBuffer.allocate(4).putInt(ckc.getReserved()).array());
        out.write(ckc.getIv().getIv());

        CkcEncryptedPayload ckcEncryptedPayload = ckc.getPayload();
        byte[] payload = ckcEncryptedPayload.getPayload();

        out.write(ByteBuffer.allocate(4).putInt(payload.length).array());
        out.write(payload);
    }
}
