package ru.devinside.drm.fairplay.ksm.spc.tags;

import java.nio.ByteBuffer;

/**
 * A combination of values that the KSM will use to encrypt the content key and the CKC payload.
 */
public class SpcSkR1 {
    private final byte[] skR1;

    // A 16-byte encrypted value used in the encryption of the content key.
    private final byte[] sessionKey;

    // A 20-byte value that represents the anonymized unique ID of the playback device.
    // However, if the value of the streaming indicator TLLV in the SPC payload is 0x5f9c8132b59f2fde,
    // then this value represents the ID of the Apple digital AV adapter and should not be used for
    // device management.
    private final byte[] hu;

    // A 44-byte random number that is used in the encryption of the CKC payload and is also returned
    // to the Apple device in the CKC payload.
    private final byte[] r1;

    // 16 bytes used to check the integrity of this SPC message.
    private final byte[] integrity;

    public SpcSkR1(byte[] skR1) {
        if(skR1.length != 96) {
            throw new IllegalArgumentException("Wrong skR1 size! 96 bytes expected.");
        }

        this.skR1 = skR1;

        ByteBuffer buffer = ByteBuffer.wrap(skR1);

        sessionKey = new byte[16];
        buffer.get(sessionKey);

        hu = new byte[20];
        buffer.get(hu);

        r1 = new byte[44];
        buffer.get(r1);

        integrity = new byte[16];
        buffer.get(integrity);
    }

    public byte[] getSkR1() {
        return skR1;
    }

    public byte[] getSessionKey() {
        return sessionKey;
    }

    public byte[] getHu() {
        return hu;
    }

    public byte[] getR1() {
        return r1;
    }

    public byte[] getIntegrity() {
        return integrity;
    }
}
