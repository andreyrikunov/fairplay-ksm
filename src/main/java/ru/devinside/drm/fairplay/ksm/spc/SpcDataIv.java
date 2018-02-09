package ru.devinside.drm.fairplay.ksm.spc;

import java.nio.ByteBuffer;

/**
 * SPC data initialization vector.
 * A CBC initialization vector that has a unique value for each SPC message.
 */
@Deprecated
public class SpcDataIv {
    public final static int SPC_DATA_IV_SIZE = 16;

    private final byte[] iv;

    public SpcDataIv(byte[] iv) {
        this.iv = iv;
    }

    public byte[] getIv() {
        return iv;
    }

    public static SpcDataIv from(ByteBuffer buffer) {
        byte[] iv = new byte[SPC_DATA_IV_SIZE];
        buffer.get(iv);
        return new SpcDataIv(iv);
    }
}
