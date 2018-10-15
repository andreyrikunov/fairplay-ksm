package ru.devinside.drm.fairplay.ksm.spc;

public enum SpcFixedLengthField {
    VERSION(4),
    RESERVED(4),

    // SPC data initialization vector. A CBC initialization vector that has a unique value for each SPC message.
    SPC_DATA_IV(16),

    // Encrypted AES-128 key. The key for decrypting the SPC payload.
    // This key is itself encrypted, using RSA public key encryption with Optimal Asymmetric Encryption Padding.
    ENCRYPTED_SPC_KEY(128),

    // The SHA-1 hash value of the encrypted Application Certificate,
    // which identifies the private key of the application that generated the SPC.
    CERTIFICATE_HASH(20);

    private final int size;

    SpcFixedLengthField(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }
}
