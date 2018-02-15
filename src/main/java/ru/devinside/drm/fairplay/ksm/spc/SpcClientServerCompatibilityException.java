package ru.devinside.drm.fairplay.ksm.spc;

public class SpcClientServerCompatibilityException extends RuntimeException {
    public SpcClientServerCompatibilityException(String message) {
        super(message);
    }

    public SpcClientServerCompatibilityException(String message, Throwable cause) {
        super(message, cause);
    }
}
