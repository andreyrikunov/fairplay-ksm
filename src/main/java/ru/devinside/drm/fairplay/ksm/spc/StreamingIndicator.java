package ru.devinside.drm.fairplay.ksm.spc;

public enum StreamingIndicator {
    AIRPLAY_TO_APPLE_TV, // Content will be sent by AirPlay to an Apple TV box
    DIGITAL_AV_ADAPTER, // Content will be sent to an Apple digital AV adapter
    ON_DEVICE; // Content playback will occur on the requesting device

    public static StreamingIndicator from(long value) {
        if(value == 0xabb0256a31843974L) {
            return AIRPLAY_TO_APPLE_TV;
        } else if(value == 0x5f9c8132b59f2fdeL) {
            return DIGITAL_AV_ADAPTER;
        } else {
            return ON_DEVICE;
        }
    }
}
