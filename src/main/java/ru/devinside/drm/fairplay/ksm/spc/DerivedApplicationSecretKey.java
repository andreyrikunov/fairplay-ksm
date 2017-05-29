package ru.devinside.drm.fairplay.ksm.spc;

import java.nio.ByteBuffer;

/**
 * Stub. Apple provides.
 */
public class DerivedApplicationSecretKey {
    public byte[] getKey() {
        ByteBuffer buffer = ByteBuffer.allocate(16);
        buffer.putInt(0xd87ce7a2);
        buffer.putInt(0x6081de2e);
        buffer.putInt(0x8eb8acef);
        buffer.putInt(0x3a6dc179);
        return buffer.array();
    }
}
