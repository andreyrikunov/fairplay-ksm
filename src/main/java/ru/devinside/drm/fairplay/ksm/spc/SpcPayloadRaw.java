package ru.devinside.drm.fairplay.ksm.spc;

import java.nio.ByteBuffer;

/**
 * SPC encrypted payload.
 */
@Deprecated
public class SpcPayloadRaw {
    private final byte[] payload;

    public SpcPayloadRaw(byte[] payload) {
        this.payload = payload;
    }

    public byte[] getPayload() {
        return payload;
    }

    public static SpcPayloadRaw from(ByteBuffer buffer) {
        // The number of bytes in the encrypted SPC payload.
        // Because the payload consists of blocks whose lengths are multiples of 16 bytes, this number is a multiple of 16.
        int payloadLength = buffer.getInt();

        byte[] payload = new byte[payloadLength];
        buffer.get(payload);
        return new SpcPayloadRaw(payload);
    }
}
