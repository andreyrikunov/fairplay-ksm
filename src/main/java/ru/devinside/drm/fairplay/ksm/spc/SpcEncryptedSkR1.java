package ru.devinside.drm.fairplay.ksm.spc;

import ru.devinside.drm.fairplay.ksm.common.TllvBlock;

import java.nio.ByteBuffer;

/**
 * Encrypted SPC [SK...R1]
 *
 * @see ru.devinside.drm.fairplay.ksm.spc.tags.SpcSkR1
 */
public class SpcEncryptedSkR1 {
    private final byte[] iv;
    private final byte[] payload;

    public SpcEncryptedSkR1(TllvBlock tllvBlock) {
        if(SpcTag.SK_R1 != SpcTag.valueOf(tllvBlock.getTag())) {
            throw new IllegalArgumentException("TLLV Block is not [SK_R1]");
        }

        ByteBuffer buffer = ByteBuffer.wrap(tllvBlock.getValue());
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
