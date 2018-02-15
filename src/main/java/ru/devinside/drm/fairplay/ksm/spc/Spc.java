package ru.devinside.drm.fairplay.ksm.spc;

import ru.devinside.drm.fairplay.ksm.common.BinVal;

/**
 * Server Playback Context
 */
public class Spc {
    private final int version;
    private final BinVal spcDataIv;
    private final BinVal encryptedSpcKey;
    private final BinVal certificateHash;
    private final BinVal encryptedPayload;

    private Spc(
            int version,
            BinVal spcDataIv,
            BinVal encryptedSpcKey,
            BinVal certificateHash,
            BinVal encryptedPayload
    ) {
        this.version = version;
        this.spcDataIv = spcDataIv;
        this.encryptedSpcKey = encryptedSpcKey;
        this.certificateHash = certificateHash;
        this.encryptedPayload = encryptedPayload;
    }

    public int getSize() {
        return Integer.BYTES +
                Integer.BYTES  +
                spcDataIv.length() +
                encryptedSpcKey.length() +
                certificateHash.length() +
                Integer.BYTES +
                encryptedPayload.length();
    }

    public int getVersion() {
        return version;
    }

    public BinVal getSpcDataIv() {
        return spcDataIv;
    }

    public BinVal getEncryptedSpcKey() {
        return encryptedSpcKey;
    }

    public BinVal getCertificateHash() {
        return certificateHash;
    }

    public BinVal getEncryptedPayload() {
        return encryptedPayload;
    }

    @Override
    public String toString() {
        return "Spc{" +
                "ver=" + version +
                ", dataIv=" + spcDataIv +
                ", encryptedSpcKey=" + encryptedSpcKey +
                ", certificateHash=" + certificateHash +
                ", encryptedPayloadLength=" + encryptedPayload.length() +
                ", encryptedPayload=" + encryptedPayload +
                '}';
    }

    public static class Builder {
        private int version;
        private BinVal spcDataIv;
        private BinVal encryptedSpcKey;
        private BinVal certificateHash;
        private BinVal encryptedPayload;

        public Builder setVersion(int version) {
            this.version = version;
            return this;
        }

        public Builder setSpcDataIv(BinVal spcDataIv) {
            this.spcDataIv = spcDataIv;
            return this;
        }

        public Builder setEncryptedSpcKey(BinVal encryptedSpcKey) {
            this.encryptedSpcKey = encryptedSpcKey;
            return this;
        }

        public Builder setCertificateHash(BinVal certificateHash) {
            this.certificateHash = certificateHash;
            return this;
        }

        public Builder setEncryptedPayload(BinVal encryptedPayload) {
            this.encryptedPayload = encryptedPayload;
            return this;
        }

        public Spc build() {
            return new Spc(version, spcDataIv, encryptedSpcKey, certificateHash, encryptedPayload);
        }
    }
}
