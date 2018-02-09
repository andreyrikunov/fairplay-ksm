package ru.devinside.drm.fairplay.ksm.common;

import ru.devinside.util.Hexler;

/**
 * Binary value (byte array wrapper)
 */
public class BinVal {
    private final byte[] value;

    public BinVal(byte[] value) {
        this.value = value;
    }

    public byte[] getBytes() {
        return value;
    }

    public int length() {
        return value.length;
    }

    @Override
    public String toString() {
        return Hexler.toHexString(value);
    }
}
