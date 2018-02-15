package ru.devinside.drm.fairplay.ksm.spc;

public class SpcSecurityException extends RuntimeException {
    public SpcSecurityException(String message) {
        super(message);
    }

    public SpcSecurityException(String message, Throwable cause) {
        super(message, cause);
    }
}
