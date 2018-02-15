package ru.devinside.drm.fairplay.ksm.ckc;

public class CkcSecurityException extends RuntimeException {
    public CkcSecurityException(String message, Throwable cause) {
        super(message, cause);
    }

    public CkcSecurityException(Throwable cause) {
        super(cause);
    }
}
