package ru.devinside.drm.fairplay.ksm.spc;

public class BadSpcException extends RuntimeException {
    public BadSpcException(String message) {
        super(message);
    }

    public BadSpcException(String message, Throwable cause) {
        super(message, cause);
    }
}
