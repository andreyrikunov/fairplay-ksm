package ru.devinside.drm.fairplay.ksm.secret;

import ru.devinside.drm.fairplay.ksm.ckc.CkcSecurityException;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class RandomContentKey implements ContentKey {
    public byte[] getKey() {
        byte[] ck = new byte[16];
        try {
            SecureRandom.getInstanceStrong().nextBytes(ck);
        } catch (NoSuchAlgorithmException e) {
            throw new CkcSecurityException(e);
        }
        return ck;
    }

    @Override
    public byte[] getIv() {
        return new byte[] {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    }
}
