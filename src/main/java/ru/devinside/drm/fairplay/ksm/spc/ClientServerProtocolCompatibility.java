package ru.devinside.drm.fairplay.ksm.spc;

import java.util.Collection;

public interface ClientServerProtocolCompatibility {
    boolean isCompatible();
    boolean isServerProtocolUpToDate();
    boolean isSuspiciousVersionUsed();

    int getClientUsedVersion();

    Collection<Integer> getClientSupportedVersions();

    Collection<Integer> getServerSupportedVersions();
}
