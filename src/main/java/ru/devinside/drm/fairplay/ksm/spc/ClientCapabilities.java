package ru.devinside.drm.fairplay.ksm.spc;

/**
 * The Capabilities TLLV was introduced in iOS 11 and communicates features supported by the client to KSM.
 */
public class ClientCapabilities {
    // When set means that the client can enforce the HDCP restrictions given in the HDCP Enforcement TLLV.
    private final boolean hdcpEnforcement;

    // When set means that the client is capable of handling and enforcing the Offline key TLLV.
    private final boolean offlineKey;

    public ClientCapabilities(boolean offlineKey, boolean hdcpEnforcement) {
        this.offlineKey = offlineKey;
        this.hdcpEnforcement = hdcpEnforcement;
    }

    public boolean isHdcpEnforcement() {
        return hdcpEnforcement;
    }

    public boolean isOfflineKey() {
        return offlineKey;
    }
}
