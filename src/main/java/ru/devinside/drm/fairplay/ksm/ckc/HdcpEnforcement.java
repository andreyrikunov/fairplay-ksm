package ru.devinside.drm.fairplay.ksm.ckc;

/**
 * High-bandwidth Digital Content Protection. Default enforcement is HDCP Type 0.
 */
public enum HdcpEnforcement {
    NONE(0xEF72894CA7895B78L),
    TYPE_0(0x40791AC78BD5C571L), // HD 1080p
    TYPE_1(0x285A0863BBA8E1D3L); // UHD

    private final long level;

    HdcpEnforcement(long level) {
        this.level = level;
    }

    public long getLevel() {
        return level;
    }
}
