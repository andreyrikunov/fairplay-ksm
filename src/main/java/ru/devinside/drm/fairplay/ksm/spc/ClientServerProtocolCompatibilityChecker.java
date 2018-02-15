package ru.devinside.drm.fairplay.ksm.spc;

import java.util.Collection;
import java.util.Collections;

public class ClientServerProtocolCompatibilityChecker {
    public static final ClientServerProtocolCompatibilityChecker INSTANCE = new ClientServerProtocolCompatibilityChecker();

    public ClientServerProtocolCompatibility check(
            Collection<Integer> serverProtocolVersion,
            int clientUsed,
            Collection<Integer> clientSupported
    ) {
        return new ClientServerProtocolCompatibility() {
            @Override
            public boolean isCompatible() {
                return serverProtocolVersion.contains(clientUsed);
            }

            @Override
            public boolean isServerProtocolUpToDate() {
                return Collections.max(clientSupported) <= Collections.max(serverProtocolVersion);
            }

            @Override
            public boolean isSuspiciousVersionUsed() {
                return clientUsed != Collections.max(clientSupported) &&
                        clientUsed != Collections.max(serverProtocolVersion);
            }
        };
    }
}
