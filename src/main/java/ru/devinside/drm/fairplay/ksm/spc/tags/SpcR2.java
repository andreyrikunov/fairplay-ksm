package ru.devinside.drm.fairplay.ksm.spc.tags;

import ru.devinside.drm.fairplay.ksm.spc.SpcTag;
import ru.devinside.drm.fairplay.ksm.common.TllvBlock;

import java.nio.ByteBuffer;

/**
 * Represents the contents of the R2 block in the SPC payload.
 */
public class SpcR2 {
    private final byte[] r2;

    public SpcR2(TllvBlock spcR2Tag) {
        if(SpcTag.R2 != SpcTag.valueOf(spcR2Tag.getTag())) {
            throw new IllegalArgumentException("TLLV Block is not R2");
        }

        ByteBuffer buffer = ByteBuffer.wrap(spcR2Tag.getValue());
        r2 = new byte[21];
        buffer.get(r2);
    }

    public byte[] getR2() {
        return r2;
    }
}
