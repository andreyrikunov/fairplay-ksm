package ru.devinside.util;

import ru.devinside.drm.fairplay.ksm.spc.DecryptionException;
import ru.devinside.drm.fairplay.ksm.spc.EncryptionException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

public class CryptoUtils {
    public static Key aesKey(byte[] key) {
        return new SecretKeySpec(key, "AES");
    }

    public static AlgorithmParameterSpec aesIv(byte[] iv) {
        return new IvParameterSpec(iv);
    }

    public static byte[] encryptWithAesEcbNoPadding(Key key, byte[] data) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/NOPADDING");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(data);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | BadPaddingException |
                IllegalBlockSizeException | InvalidKeyException e ) {
            throw new EncryptionException(e);
        }
    }

    public static byte[] encryptWithAesCbcNoPadding(Key key, AlgorithmParameterSpec iv, byte[] data) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/NOPADDING");
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);
            return cipher.doFinal(data);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | BadPaddingException |
                IllegalBlockSizeException | InvalidKeyException | InvalidAlgorithmParameterException e) {
            throw new EncryptionException(e);
        }
    }

    public static Key rsaPrivateKey(byte[] key) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(key));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new DecryptionException(e);
        }
    }

    public static byte[] decryptWithRsaEcbOaepPadding(Key key, byte[] data) {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPPadding");
            cipher.init(Cipher.DECRYPT_MODE, key);
            return cipher.doFinal(data);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | BadPaddingException |
                IllegalBlockSizeException | InvalidKeyException e ) {
            throw new DecryptionException(e);
        }
    }

    public static byte[] decryptWithAesCbcNoPadding(Key key, AlgorithmParameterSpec iv, byte[] data) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/NOPADDING");
            cipher.init(Cipher.DECRYPT_MODE, key, iv);
            return cipher.doFinal(data);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | BadPaddingException |
                IllegalBlockSizeException | InvalidKeyException | InvalidAlgorithmParameterException e ) {
            throw new DecryptionException(e);
        }
    }
}
