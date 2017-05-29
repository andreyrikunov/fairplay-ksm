package ru.devinside.drm.fairplay.ksm.ckc;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class CkcMessageSerializer {
    private final CkcMessage ckcMessage;

    public CkcMessageSerializer(CkcMessage ckcMessage) {
        this.ckcMessage = ckcMessage;
    }

    public void serializeTo(OutputStream out) throws IOException {
        out.write(ByteBuffer.allocate(4).putInt(ckcMessage.getVersion()).array());
        out.write(ByteBuffer.allocate(4).putInt(ckcMessage.getReserved()).array());
        out.write(ckcMessage.getIv().getIv());

        CkcEncryptedPayload ckcEncryptedPayload = ckcMessage.getPayload();
        byte[] payload = ckcEncryptedPayload.getPayload();

        out.write(ByteBuffer.allocate(4).putInt(payload.length).array());
        out.write(payload);
    }
}
