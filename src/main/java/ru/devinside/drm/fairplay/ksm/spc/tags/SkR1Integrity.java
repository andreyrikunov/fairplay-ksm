package ru.devinside.drm.fairplay.ksm.spc.tags;

import ru.devinside.drm.fairplay.ksm.spc.SpcTag;
import ru.devinside.drm.fairplay.ksm.common.TllvBlock;

import java.nio.ByteBuffer;

public class SkR1Integrity {
    private final byte[] skR1Integrity;

    public SkR1Integrity(TllvBlock skR1IntegrityTag) {
        if(SpcTag.SK_R1_INTEGRITY != SpcTag.valueOf(skR1IntegrityTag.getTag())) {
            throw new IllegalArgumentException("TLLV Block is not SK_R1_INTEGRITY");
        }

        ByteBuffer buffer = ByteBuffer.wrap(skR1IntegrityTag.getValue());
        skR1Integrity = new byte[16];
        buffer.get(skR1Integrity);
    }

    public byte[] getIntegrity() {
        return skR1Integrity;
    }
}
