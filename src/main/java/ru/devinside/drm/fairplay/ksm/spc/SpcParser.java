package ru.devinside.drm.fairplay.ksm.spc;

import ru.devinside.drm.fairplay.ksm.common.BinVal;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

public class SpcParser {
    public Spc parse(byte[] spc) {
        ByteBuffer buf = ByteBuffer.wrap(spc);

        int version = parseVersion(buf);

        buf.getInt(); // Reserved for Apple; ignore these bytes

        BinVal spcDataIv = parseField(buf, SpcFixedLengthField.SPC_DATA_IV);
        BinVal encryptedSpcKey = parseField(buf, SpcFixedLengthField.ENCRYPTED_SPC_KEY);
        BinVal certificateHash = parseField(buf, SpcFixedLengthField.CERTIFICATE_HASH);
        BinVal encryptedPayload = parseField(buf, SpcVariableLengthField.ENCRYPTED_PAYLOAD);

        return new Spc.Builder()
                .setVersion(version)
                .setSpcDataIv(spcDataIv)
                .setEncryptedSpcKey(encryptedSpcKey)
                .setCertificateHash(certificateHash)
                .setEncryptedPayload(encryptedPayload)
                .build();
    }

    private static int parseVersion(ByteBuffer buf) {
        try {
            return buf.getInt();
        } catch (BufferUnderflowException e) {
            throw new BadSpcException("Unable to parse SPC version", e);
        }
    }

    private static BinVal parseField(ByteBuffer buf, SpcVariableLengthField field) {
        try {
            return SpcFieldFactory.from(buf, field);
        } catch (BufferUnderflowException | IllegalArgumentException e) {
            throw new BadSpcException(String.format("Unable to parse SPC variable-length field: %s", field), e);
        }
    }

    private static BinVal parseField(ByteBuffer buf, SpcFixedLengthField field) {
        try {
            return SpcFieldFactory.from(buf, field);
        } catch (BufferUnderflowException | IllegalArgumentException e) {
            throw new BadSpcException(String.format("Unable to parse SPC fixed-length field: %s", field), e);
        }
    }
}
