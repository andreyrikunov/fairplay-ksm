package ru.devinside.drm.fairplay.ksm.spc;

public class DerivedApplicationSecretKey {
    private final byte[] dask;

    public DerivedApplicationSecretKey(byte[] dask) {
        this.dask = dask;
    }

    public byte[] getKey() {
        return dask;
    }
}
