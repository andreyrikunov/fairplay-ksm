package ru.devinside.drm.fairplay.ksm.spc;

import ru.devinside.drm.fairplay.ksm.common.BinVal;

public class AssetId {
    private final BinVal val;

    public AssetId(BinVal val) {
        this.val = val;
    }

    public BinVal getBinVal() {
        return val;
    }
}
