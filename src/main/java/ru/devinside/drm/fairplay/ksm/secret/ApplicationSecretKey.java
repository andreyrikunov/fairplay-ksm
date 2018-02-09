package ru.devinside.drm.fairplay.ksm.secret;

/**
 * The ASk (Application Secret key) and FairPlay Streaming (FPS) certificate are used together to secure the
 * Content Key in the FPS protocol.
 */
public final class ApplicationSecretKey {
    public final static ApplicationSecretKey STUB = new ApplicationSecretKey(new byte[]{});

    private final byte[] value;

    public ApplicationSecretKey(byte[] value) {
        this.value = value;
    }

    byte[] getBytes() {
        return value.clone();
    }

    @Override
    public final String toString() {
        return "***************";
    }
}
