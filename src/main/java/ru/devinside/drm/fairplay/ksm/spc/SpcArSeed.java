package ru.devinside.drm.fairplay.ksm.spc;

import ru.devinside.drm.fairplay.ksm.common.TllvBlock;

public class SpcArSeed {
    private final byte[] arSeed;

    public SpcArSeed(TllvBlock arSeedBlock) {
        if(SpcTag.AR_SEED != SpcTag.valueOf(arSeedBlock.getTag())) {
            throw new IllegalArgumentException("TLLV Block is not AR_SEED");
        }

        arSeed = arSeedBlock.getValue();
    }

    public byte[] getArSeed() {
        return arSeed;
    }
}
