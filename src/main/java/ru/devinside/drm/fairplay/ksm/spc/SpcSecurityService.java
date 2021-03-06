package ru.devinside.drm.fairplay.ksm.spc;

import ru.devinside.drm.fairplay.ksm.common.BinVal;
import ru.devinside.drm.fairplay.ksm.secret.DFunction;
import ru.devinside.drm.fairplay.ksm.secret.FpsCertificate;
import ru.devinside.drm.fairplay.ksm.spc.tags.SkR1Integrity;
import ru.devinside.drm.fairplay.ksm.spc.tags.SpcR2;
import ru.devinside.drm.fairplay.ksm.spc.tags.SpcSkR1;
import ru.devinside.util.CryptoUtils;

import java.security.Key;
import java.util.Arrays;

public class SpcSecurityService {
    private final FpsCertificate certificate;
    private final DFunction dFunction;
    private final byte[] appCertificatePrivateKey;

    public SpcSecurityService(FpsCertificate certificate, DFunction dFunction, byte[] appCertificatePrivateKey) {
        this.certificate = certificate;
        this.dFunction = dFunction;
        this.appCertificatePrivateKey = appCertificatePrivateKey;
    }

    public void ensureCorrectFpsCertificateUsed(Spc spc) {
        if(!certificate.checkHash(spc.getCertificateHash())) {
            throw new SpcSecurityException("Unknown FPS certificate");
        }
    }

    public SpcKey decryptSpcKey(BinVal encryptedSpcKey) {
        try {
            Key key = CryptoUtils.rsaPrivateKey(appCertificatePrivateKey);
            return new SpcKey(CryptoUtils.decryptWithRsaEcbOaepPadding(key, encryptedSpcKey.getBytes()));
        } catch (DecryptionException e) {
            throw new SpcSecurityException("Unable to decrypt SPC encryption key", e);
        }
    }

    public SpcPayload decryptPayload(Spc spc, SpcKey spcKey) {
        try {
            return new SpcPayload(
                    CryptoUtils.decryptWithAesCbcNoPadding(
                            CryptoUtils.aesKey(spcKey.getSpck()),
                            CryptoUtils.aesIv(spc.getSpcDataIv().getBytes()),
                            spc.getEncryptedPayload().getBytes()
                    )
            );
        } catch (DecryptionException e) {
            throw new SpcSecurityException("Unable to decrypt SPC payload", e);
        }
    }

    public SpcSkR1 decryptSkR1(SpcEncryptedSkR1 spcEncryptedSkR1, SpcR2 r2) {
        try {
            DerivedApplicationSecretKey derivedApplicationSecretKey = dFunction.derive(r2);
            return new SpcSkR1(
                    CryptoUtils.decryptWithAesCbcNoPadding(
                            CryptoUtils.aesKey(derivedApplicationSecretKey.getKey()),
                            CryptoUtils.aesIv(spcEncryptedSkR1.getIv()),
                            spcEncryptedSkR1.getPayload()
                    )
            );
        } catch (EncryptionException e) {
            throw new SpcSecurityException("Unable to derive application key", e);
        } catch (DecryptionException e) {
            throw new SpcSecurityException("Unable to decrypt [SK..R1]", e);
        }
    }

    public boolean integrityCheckSkR1(SpcSkR1 skR1, SkR1Integrity skR1Integrity) {
        return Arrays.equals(skR1.getIntegrity(), skR1Integrity.getIntegrity());
    }

    public FpsCertificate getCertificate() {
        return certificate;
    }
}
