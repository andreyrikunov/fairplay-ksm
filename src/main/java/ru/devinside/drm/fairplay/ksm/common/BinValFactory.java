package ru.devinside.drm.fairplay.ksm.common;

import java.nio.ByteBuffer;

public class BinValFactory {
    public static BinVal fixedLength(ByteBuffer buffer, int length) {
        byte[] value = new byte[length];
        buffer.get(value);
        return new BinVal(value);
    }

    public static BinVal variableLength(ByteBuffer buffer, BinValPadding padding) {
        int length = buffer.getInt();

        if(length % padding.getPaddingBytesNum() != 0) {
            throw new IllegalArgumentException(
                    String.format("The number of bytes must be a multiple of %s", padding.getPaddingBytesNum())
            );
        }

        byte[] payload = new byte[length];
        buffer.get(payload);
        return new BinVal(payload);
    }
}
