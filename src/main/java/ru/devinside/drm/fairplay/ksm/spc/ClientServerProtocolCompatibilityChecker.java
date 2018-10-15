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
        return new ClientServerProtocolCompatibilityImpl(serverProtocolVersion, clientUsed, clientSupported);
    }

    private static class ClientServerProtocolCompatibilityImpl implements ClientServerProtocolCompatibility {
        private final Collection<Integer> serverProtocolVersion;
        private final int clientUsed;
        private final Collection<Integer> clientSupported;

        ClientServerProtocolCompatibilityImpl(
                Collection<Integer> serverProtocolVersion,
                int clientUsed,
                Collection<Integer> clientSupported
        ) {
            this.serverProtocolVersion = serverProtocolVersion;
            this.clientUsed = clientUsed;
            this.clientSupported = clientSupported;
        }

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

        @Override
        public int getClientUsedVersion() {
            return clientUsed;
        }

        @Override
        public Collection<Integer> getClientSupportedVersions() {
            return clientSupported;
        }

        @Override
        public Collection<Integer> getServerSupportedVersions() {
            return serverProtocolVersion;
        }
    }
}
