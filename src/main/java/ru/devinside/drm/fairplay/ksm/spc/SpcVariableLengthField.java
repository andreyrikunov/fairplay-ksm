package ru.devinside.drm.fairplay.ksm.spc;

import ru.devinside.drm.fairplay.ksm.common.BinValPadding;

public enum SpcVariableLengthField {
    ENCRYPTED_PAYLOAD(BinValPadding.PAD16);

    private final BinValPadding padding;

    SpcVariableLengthField(BinValPadding padding) {
        this.padding = padding;
    }

    public BinValPadding getPadding() {
        return padding;
    }
}
