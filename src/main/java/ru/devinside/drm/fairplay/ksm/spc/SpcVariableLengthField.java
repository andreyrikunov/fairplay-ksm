package ru.devinside.drm.fairplay.ksm.spc;

import ru.devinside.drm.fairplay.ksm.common.BinValPadding;

public enum SpcVariableLengthField {
    ENCRYPTED_PAYLOAD;

    private BinValPadding padding;

    static {
        ENCRYPTED_PAYLOAD.padding = BinValPadding.PAD16;
    }

    public BinValPadding getPadding() {
        return padding;
    }
}