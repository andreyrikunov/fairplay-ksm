package ru.devinside.drm.fairplay.ksm.spc;

import ru.devinside.drm.fairplay.ksm.common.BinVal;
import ru.devinside.drm.fairplay.ksm.secret.DFunction;
import ru.devinside.drm.fairplay.ksm.spc.tags.SkR1Integrity;
import ru.devinside.drm.fairplay.ksm.spc.tags.SpcR2;
import ru.devinside.drm.fairplay.ksm.spc.tags.SpcSkR1;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Arrays;

public class SpcSecurityService {
    private final DFunction dFunction;
    private final byte[] appCertificatePrivateKey;

    public SpcSecurityService(DFunction dFunction, byte[] appCertificatePrivateKey) {
        this.dFunction = dFunction;
        this.appCertificatePrivateKey = appCertificatePrivateKey;
    }

    public SpcKey decryptSpcKey(BinVal encryptedSpcKey) {
        try {
            Cipher rsaCipher = Cipher.getInstance("RSA/ECB/OAEPPadding");
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            KeySpec keySpec = new PKCS8EncodedKeySpec(appCertificatePrivateKey);
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
            rsaCipher.init(Cipher.DECRYPT_MODE, privateKey);
            return new SpcKey(rsaCipher.doFinal(encryptedSpcKey.getBytes()));
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | BadPaddingException |
                IllegalBlockSizeException | InvalidKeyException | InvalidKeySpecException e ) {
            throw new SpcSecurityException(e);
        }
    }

    public SpcPayload decryptPayload(Spc spc, SpcKey spcKey) {
        SecretKey secretKeyKey = new SecretKeySpec(spcKey.getSpck(), "AES");
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/NOPADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKeyKey, new IvParameterSpec(spc.getSpcDataIv().getBytes()));
            return new SpcPayload(cipher.doFinal(spc.getEncryptedPayload().getBytes()));
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | BadPaddingException |
                IllegalBlockSizeException | InvalidKeyException | InvalidAlgorithmParameterException e ) {
            throw new SpcSecurityException(e);
        }
    }

    public SpcSkR1 decryptSkR1(SpcSkR1Raw spcSkR1Raw, SpcR2 r2) {
        DerivedApplicationSecretKey derivedApplicationSecretKey = dFunction.derive(r2);

        SecretKey secretKeyKey = new SecretKeySpec(derivedApplicationSecretKey.getKey(), "AES");
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/NOPADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKeyKey, new IvParameterSpec(spcSkR1Raw.getIv()));
            return new SpcSkR1(cipher.doFinal(spcSkR1Raw.getPayload()));
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | BadPaddingException |
                IllegalBlockSizeException | InvalidKeyException | InvalidAlgorithmParameterException e ) {
            throw new SpcSecurityException(e);
        }
    }

    public boolean integrityCheckSkR1(SpcSkR1 skR1, SkR1Integrity skR1Integrity) {
        return Arrays.equals(skR1.getIntegrity(), skR1Integrity.getIntegrity());
    }
}
