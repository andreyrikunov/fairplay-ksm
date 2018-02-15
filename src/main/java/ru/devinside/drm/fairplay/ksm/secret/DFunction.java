package ru.devinside.drm.fairplay.ksm.secret;

import ru.devinside.drm.fairplay.ksm.spc.DerivedApplicationSecretKey;
import ru.devinside.drm.fairplay.ksm.spc.tags.SpcR2;
import ru.devinside.util.CryptoUtils;

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
                CryptoUtils.encryptWithAesEcbNoPadding(
                        CryptoUtils.aesKey(ask.getBytes()),
                        computeHash(spcR2.getR2())
                )
        );
    }
}
