package ru.devinside.drm.fairplay.ksm.spc;

public interface ClientServerProtocolCompatibility {
    boolean isCompatible();
    boolean isServerProtocolUpToDate();
    boolean isSuspiciousVersionUsed();
}
