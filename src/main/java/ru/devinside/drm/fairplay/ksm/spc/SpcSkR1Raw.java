package ru.devinside.drm.fairplay.ksm.spc;

import ru.devinside.drm.fairplay.ksm.common.TllvBlock;

import java.nio.ByteBuffer;

/**
 * Encrypted SPC SK...R1
 *
 * @see ru.devinside.drm.fairplay.ksm.spc.tags.SpcSkR1
 */
public class SpcSkR1Raw {
    private final byte[] iv;
    private final byte[] payload;

    public SpcSkR1Raw(TllvBlock spcSkR1Tag) {
        if(SpcTag.SK_R1 != SpcTag.valueOf(spcSkR1Tag.getTag())) {
            throw new IllegalArgumentException("TLLV Block is not SK_R1");
        }

        ByteBuffer buffer = ByteBuffer.wrap(spcSkR1Tag.getValue());
        iv = new byte[16];
        buffer.get(iv);
        payload = new byte[96];
        buffer.get(payload);
    }

    public byte[] getIv() {
        return iv;
    }

    public byte[] getPayload() {
        return payload;
    }
}
