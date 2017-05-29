package ru.devinside.drm.fairplay.ksm.spc;

/**
 * Server Playback Context (SPC) raw message. Nothing decrypted yet.
 */
public class SpcMessage {
    private int version; // The version number of the SPC.

    private int reserved; // Reserved for Apple; ignore these bytes

    private SpcDataIv iv;

    private SpckRaw encryptedSpck;

    private SpcApplicationCertificateHash certificateHash;

    // SPC encrypted payload
    private SpcPayloadRaw payload;

    public SpcMessage(
            int version,
            int reserved,
            SpcDataIv iv,
            SpckRaw encryptedSpck,
            SpcApplicationCertificateHash certificateHash,
            SpcPayloadRaw payload
    ) {
        this.version = version;
        this.reserved = reserved;
        this.iv = iv;
        this.encryptedSpck = encryptedSpck;
        this.certificateHash = certificateHash;
        this.payload = payload;
    }

    public int getVersion() {
        return version;
    }

    public int getReserved() {
        return reserved;
    }

    public SpcDataIv getIv() {
        return iv;
    }

    public SpckRaw getEncryptedSpck() {
        return encryptedSpck;
    }

    public SpcApplicationCertificateHash getCertificateHash() {
        return certificateHash;
    }

    public SpcPayloadRaw getPayload() {
        return payload;
    }
}
