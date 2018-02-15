package ru.devinside.drm.fairplay.ksm.secret;

import ru.devinside.drm.fairplay.ksm.ckc.ContentKeyIv;

import java.util.Random;

public class RandomContentKey implements ContentKey {
    public byte[] getKey() {
        byte[] ck = new byte[ContentKey.CONTENT_KEY_SIZE];
        new Random().nextBytes(ck);
        return ck;
    }

    @Override
    public byte[] getIv() {
        byte[] iv = new byte[ContentKeyIv.CONTENT_KEY_DATA_IV_SIZE];
        new Random().nextBytes(iv);
        return iv;
    }
}
