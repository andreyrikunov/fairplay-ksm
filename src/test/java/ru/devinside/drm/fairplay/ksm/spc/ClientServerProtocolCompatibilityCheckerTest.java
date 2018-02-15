package ru.devinside.drm.fairplay.ksm.spc;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class ClientServerProtocolCompatibilityCheckerTest {
    private static final int SERVER_VER_1 = 1;
    private static final int SERVER_VER_2 = 2;

    private static final int CLIENT_VER_1 = 1;
    private static final int CLIENT_VER_2 = 2;

    @Test
    public void clientEqualsServerSingleVersion() {
        ClientServerProtocolCompatibility compatibility = ClientServerProtocolCompatibilityChecker.INSTANCE.check(
                Collections.singleton(SERVER_VER_1),
                CLIENT_VER_1,
                Collections.singleton(CLIENT_VER_1)
        );

        assertEquals(true, compatibility.isCompatible());
        assertEquals(true, compatibility.isServerProtocolUpToDate());
        assertEquals(false, compatibility.isSuspiciousVersionUsed());
    }

    @Test
    public void serverIsNewerMultipleVersions() {
        ClientServerProtocolCompatibility compatibility = ClientServerProtocolCompatibilityChecker.INSTANCE.check(
                Arrays.asList(SERVER_VER_1, SERVER_VER_2),
                CLIENT_VER_1,
                Collections.singleton(CLIENT_VER_1)
        );

        assertEquals(true, compatibility.isCompatible());
        assertEquals(true, compatibility.isServerProtocolUpToDate());
        assertEquals(false, compatibility.isSuspiciousVersionUsed());
    }

    @Test
    public void clientEqualsServerMultipleVersionsUsingLatest() {
        ClientServerProtocolCompatibility compatibility = ClientServerProtocolCompatibilityChecker.INSTANCE.check(
                Arrays.asList(SERVER_VER_1, SERVER_VER_2),
                CLIENT_VER_2,
                Arrays.asList(CLIENT_VER_1, CLIENT_VER_2)
        );

        assertEquals(true, compatibility.isCompatible());
        assertEquals(true, compatibility.isServerProtocolUpToDate());
        assertEquals(false, compatibility.isSuspiciousVersionUsed());
    }

    @Test
    public void clientHasMoreVersionsIncludingServerVersionUsingLatest() {
        ClientServerProtocolCompatibility compatibility = ClientServerProtocolCompatibilityChecker.INSTANCE.check(
                Collections.singleton(SERVER_VER_2),
                CLIENT_VER_2,
                Arrays.asList(CLIENT_VER_1, CLIENT_VER_2)
        );

        assertEquals(true, compatibility.isCompatible());
        assertEquals(true, compatibility.isServerProtocolUpToDate());
        assertEquals(false, compatibility.isSuspiciousVersionUsed());
    }

    @Test
    public void serverIsOutdated() {
        ClientServerProtocolCompatibility compatibility = ClientServerProtocolCompatibilityChecker.INSTANCE.check(
                Collections.singleton(SERVER_VER_1),
                CLIENT_VER_1,
                Arrays.asList(CLIENT_VER_1, CLIENT_VER_2)
        );

        assertEquals(true, compatibility.isCompatible());
        assertEquals(false, compatibility.isServerProtocolUpToDate());
        assertEquals(false, compatibility.isSuspiciousVersionUsed());
    }

    @Test
    public void serverShouldRejectClient() {
        ClientServerProtocolCompatibility compatibility = ClientServerProtocolCompatibilityChecker.INSTANCE.check(
                Collections.singleton(SERVER_VER_2),
                CLIENT_VER_1,
                Collections.singleton(CLIENT_VER_1)
        );

        assertEquals(false, compatibility.isCompatible());
        assertEquals(true, compatibility.isServerProtocolUpToDate());
        assertEquals(false, compatibility.isSuspiciousVersionUsed());
    }

    @Test
    public void clientExploitsServerUsingOldVersion() {
        ClientServerProtocolCompatibility compatibility = ClientServerProtocolCompatibilityChecker.INSTANCE.check(
                Arrays.asList(SERVER_VER_1, SERVER_VER_2),
                CLIENT_VER_1,
                Arrays.asList(CLIENT_VER_1, CLIENT_VER_2)
        );

        assertEquals(true, compatibility.isCompatible());
        assertEquals(true, compatibility.isServerProtocolUpToDate());
        assertEquals(true, compatibility.isSuspiciousVersionUsed());
    }
}
