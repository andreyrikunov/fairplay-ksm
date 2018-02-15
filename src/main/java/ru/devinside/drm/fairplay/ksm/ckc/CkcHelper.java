package ru.devinside.drm.fairplay.ksm.ckc;

import ru.devinside.drm.fairplay.ksm.secret.ContentKey;
import ru.devinside.drm.fairplay.ksm.spc.*;
import ru.devinside.drm.fairplay.ksm.spc.tags.SpcSkR1;
import ru.devinside.util.CryptoUtils;

import java.nio.ByteBuffer;
import java.security.*;

public class CkcHelper {
    private final static int AR_KEY_LENGTH = 16;

    private final byte[] encryptedArSeed;

    public CkcHelper(SpcSkR1 skR1, SpcArSeed arSeed) {
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.update(skR1.getR1());
            byte[] arKey = new byte[AR_KEY_LENGTH];
            ByteBuffer.wrap(crypt.digest()).get(arKey);
            encryptedArSeed = CryptoUtils.encryptWithAesEcbNoPadding(CryptoUtils.aesKey(arKey), arSeed.getArSeed());
        } catch (NoSuchAlgorithmException | EncryptionException e) {
            throw new CkcSecurityException("Unable to encrypt AR seed", e);
        }

    }

    public CkcEncryptedCk encryptCk(ContentKey ck, SpcSkR1 spcSkR1) {
        try {
            Key sessionKey = CryptoUtils.aesKey(spcSkR1.getSessionKey());
            return new CkcEncryptedCk(CryptoUtils.encryptWithAesEcbNoPadding(sessionKey, ck.getKey()));
        } catch (EncryptionException e) {
            throw new CkcSecurityException("Unable to encrypt content key", e);
        }
    }

    public CkcEncryptedPayload encryptCkcPayload(CkcPayload payload, CkcDataIv iv) {
        try {
            return new CkcEncryptedPayload(
                    CryptoUtils.encryptWithAesCbcNoPadding(
                            CryptoUtils.aesKey(encryptedArSeed),
                            CryptoUtils.aesIv(iv.getIv()),
                            new CkcPayloadSerializer(payload).serialize()
                    )
            );
        } catch (EncryptionException e) {
            throw new CkcSecurityException("Unable to encrypt CKC payload", e);
        }
    }
}
