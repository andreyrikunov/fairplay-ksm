package ru.devinside.drm.fairplay.ksm.spc;

import ru.devinside.util.Hexler;

/**
 * Precomputed DASk from FPS SDK to perform SPC CKC verification tests
 */
public final class MockDerivedApplicationSecretKey extends DerivedApplicationSecretKey {
    private final static byte[] DASK = Hexler.toByteArray("d87ce7a26081de2e8eb8acef3a6dc179");

    public MockDerivedApplicationSecretKey() {
        super(new byte[] {});
    }

    @Override
    public final byte[] getKey() {
        return DASK;
    }
}
