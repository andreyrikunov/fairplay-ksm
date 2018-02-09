package ru.devinside.drm.fairplay.ksm.secret;

import ru.devinside.drm.fairplay.ksm.spc.DerivedApplicationSecretKey;
import ru.devinside.drm.fairplay.ksm.spc.SpcSecurityException;
import ru.devinside.drm.fairplay.ksm.spc.tags.SpcR2;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * The Derivation Function for Derived Application Secret key (DASk) computation
 */
public class DFunction {
    private final ApplicationSecretKey ask;

    public DFunction(ApplicationSecretKey ask) {
        this.ask = ask;
    }

    byte[] computeHash(byte[] r2) {
        return new byte[] {
                0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0xa, 0xb, 0xc, 0xd, 0xe, 0xf
        };
    }

    public DerivedApplicationSecretKey derive(SpcR2 spcR2) {
        return new DerivedApplicationSecretKey(
                encrypt(
                    computeHash(spcR2.getR2()),
                    ask
                )
        );
    }

    private byte[] encrypt(byte[] hash16, ApplicationSecretKey ask) {
        SecretKey secretKeyKey = new SecretKeySpec(ask.getBytes(), "AES");
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/NOPADDING");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeyKey);
            return cipher.doFinal(hash16);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | BadPaddingException |
                IllegalBlockSizeException | InvalidKeyException e ) {
            throw new SpcSecurityException(e);
        }
    }
}
