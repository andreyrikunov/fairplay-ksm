package ru.devinside.drm.fairplay.ksm.common;

public enum BinValPadding {
    PAD16(16);

    private final int paddingBytesNum;

    BinValPadding(int paddingBytesNum) {
        this.paddingBytesNum = paddingBytesNum;
    }

    public int getPaddingBytesNum() {
        return paddingBytesNum;
    }
}
