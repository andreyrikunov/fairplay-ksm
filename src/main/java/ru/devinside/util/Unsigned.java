package ru.devinside.util;

/**
 * Few utils for 32-bit unsigned arithmetic serving DFunction
 *
 * @see ru.devinside.drm.fairplay.ksm.secret.DFunction
 */
public class Unsigned {
    public static int toInt(byte value) {
        return value & 0xff;
    }

    public static long toLong(int value) {
        return value & 0xffffffffL;
    }

    public static int toInt(long value) {
        return (int) (value);
    }

    public static int remainder(int dividend, int divisor) {
        return (int) (toLong(dividend) % toLong(divisor));
    }
}
