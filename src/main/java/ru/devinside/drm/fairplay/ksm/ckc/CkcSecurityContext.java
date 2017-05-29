package ru.devinside.drm.fairplay.ksm.ckc;

import ru.devinside.drm.fairplay.ksm.secret.ContentKey;
import ru.devinside.drm.fairplay.ksm.spc.*;
import ru.devinside.drm.fairplay.ksm.spc.tags.SpcSkR1;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.*;

public class CkcSecurityContext {
    private final static int AR_KEY_LENGTH = 16;

    private final byte[] encryptedArSeed;

    public CkcSecurityContext(SpcSkR1 skR1, SpcArSeed arSeed) {
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.update(skR1.getR1());
            byte[] arKey = new byte[AR_KEY_LENGTH];
            ByteBuffer.wrap(crypt.digest()).get(arKey);

            encryptedArSeed = encryptArSeed(arKey, arSeed.getArSeed());
        } catch (NoSuchAlgorithmException e) {
            throw new CkcSecurityException(e);
        }

    }

    private byte[] encryptArSeed(byte[] arKey, byte[] arSeed) {
        SecretKey secretKeyKey = new SecretKeySpec(arKey, "AES");
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/NOPADDING");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeyKey);
            return cipher.doFinal(arSeed);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | BadPaddingException |
                IllegalBlockSizeException | InvalidKeyException e ) {
            throw new SpcSecurityException(e);
        }
    }

    public CkcEncryptedCk encryptCk(ContentKey ck, SpcSkR1 spcSkR1) {
        SecretKey secretKeyKey = new SecretKeySpec(spcSkR1.getSessionKey(), "AES");
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/NOPADDING");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeyKey);
            return new CkcEncryptedCk(cipher.doFinal(ck.getKey()));
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | BadPaddingException |
                IllegalBlockSizeException | InvalidKeyException e ) {
            throw new SpcSecurityException(e);
        }
    }

    public CkcEncryptedPayload encryptCkcPayload(CkcPayload payload, CkcDataIv iv) {
        SecretKey secretKeyKey = new SecretKeySpec(encryptedArSeed, "AES");
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/NOPADDING");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeyKey, new IvParameterSpec(iv.getIv()));
            return new CkcEncryptedPayload(cipher.doFinal(new CkcPayloadSerializer(payload).serialize()));
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | BadPaddingException |
                IllegalBlockSizeException | InvalidKeyException | InvalidAlgorithmParameterException | IOException e ) {
            throw new SpcSecurityException(e);
        }
    }
}
