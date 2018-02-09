package ru.devinside.drm.fairplay.ksm.spc;

import java.nio.ByteBuffer;

/**
 * The SHA-1 hash value of the encrypted Application Certificate,
 * which identifies the private key of the developer that generated the SPC.
 */
@Deprecated
public class SpcApplicationCertificateHash {
    public final static int SPC_APP_CERTIFICATE_HASH_SIZE = 20;

    private final byte[] certificateHash;

    public SpcApplicationCertificateHash(byte[] certificateHash) {
        this.certificateHash = certificateHash;
    }

    public byte[] getCertificateHash() {
        return certificateHash;
    }

    public static SpcApplicationCertificateHash from(ByteBuffer buffer) {
        byte[] certificateHash = new byte[SPC_APP_CERTIFICATE_HASH_SIZE];
        buffer.get(certificateHash);
        return new SpcApplicationCertificateHash(certificateHash);
    }
}
