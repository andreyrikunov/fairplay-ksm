package ru.devinside.drm.fairplay.ksm.spc;

import javax.xml.bind.DatatypeConverter;
import java.math.BigInteger;
import java.nio.ByteBuffer;

public class SpcMessageParser {
    public SpcMessage parse(byte[] spcRaw) {
        ByteBuffer spcRawWrapped = ByteBuffer.wrap(spcRaw);

        int version = spcRawWrapped.getInt();
        int reserved = spcRawWrapped.getInt();

        SpcDataIv spcDataIv = SpcDataIv.from(spcRawWrapped);
        SpckRaw spckRaw = SpckRaw.from(spcRawWrapped);
        SpcApplicationCertificateHash certificateHash = SpcApplicationCertificateHash.from(spcRawWrapped);
        SpcPayloadRaw spcPayloadRaw = SpcPayloadRaw.from(spcRawWrapped);

        // TODO: remove debug prints
        System.out.println("version: " + version);
        System.out.println("reserved: " + reserved);
        System.out.println("iv: " + new BigInteger(1, spcDataIv.getIv()).toString(16));
        System.out.println("aes encrypted: " + DatatypeConverter.printHexBinary(spckRaw.getSpckEncrypted()));
        System.out.println("cert hash: " + new BigInteger(1, certificateHash.getCertificateHash()).toString(16));
        System.out.println("payload length: " + spcPayloadRaw.getPayload().length);
        System.out.println("payload enc: " + new BigInteger(1, spcPayloadRaw.getPayload()).toString(16));

        return new SpcMessage(version, reserved, spcDataIv, spckRaw, certificateHash, spcPayloadRaw);
    }
}
