package ru.devinside.drm.fairplay.ksm.ckc;

/**
 * A TLLV block containing the 44-byte R1 value that was sent to the KSM in the SPC payload.
 */
public class CkcR1 {
    private final byte[] r1;

    public CkcR1(byte[] r1) {
        this.r1 = r1;
    }

    public byte[] getR1() {
        return r1;
    }
}
