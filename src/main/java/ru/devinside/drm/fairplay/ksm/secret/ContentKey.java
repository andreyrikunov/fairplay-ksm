package ru.devinside.drm.fairplay.ksm.secret;

import ru.devinside.drm.fairplay.ksm.ckc.CkcSecurityException;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class ContentKey {
    public byte[] getKey() {
        byte[] ck = new byte[16];
        try {
            SecureRandom.getInstanceStrong().nextBytes(ck);
        } catch (NoSuchAlgorithmException e) {
            throw new CkcSecurityException(e);
        }
        return ck;
    }
}
