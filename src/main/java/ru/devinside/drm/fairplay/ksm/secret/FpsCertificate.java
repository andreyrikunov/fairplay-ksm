package ru.devinside.drm.fairplay.ksm.secret;

import org.apache.commons.codec.digest.DigestUtils;
import ru.devinside.drm.fairplay.ksm.common.BinVal;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

public class FpsCertificate {
    private final byte[] certificate;
    private final byte[] hash;

    public FpsCertificate(byte[] certificate) {
        this.certificate = certificate.clone();
        this.hash = DigestUtils.sha1(certificate);
    }

    public byte[] asBytes() {
        return certificate.clone();
    }

    public byte[] hash() {
        return hash.clone();
    }

    public boolean checkHash(BinVal hash) {
        return Arrays.equals(this.hash, hash.getBytes());
    }

    public void write(OutputStream out) throws IOException {
        out.write(certificate);
    }
}
