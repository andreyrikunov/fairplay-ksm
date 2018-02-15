package ru.devinside.drm.fairplay.ksm.secret;

public interface ContentKey {
    int CONTENT_KEY_SIZE = 16;

    byte[] getKey();
    byte[] getIv();
}
