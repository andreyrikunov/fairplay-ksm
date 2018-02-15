package ru.devinside.drm.fairplay.ksm.spc;

import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class SpcParserTest {
    @Test(expected = BadSpcException.class)
    public void emptySpc() {
        new SpcParser().parse(new byte[] {});
    }

    @Test(expected = BadSpcException.class)
    public void nullSpc() {
        new SpcParser().parse(new byte[] {});
    }

    @Test
    public void fullRandomSpc() {
        int payloadLengthPad16 = 16 + new Random().nextInt(8) * 16;

        byte[] randomSpc = randomSpc(payloadLengthPad16);
        Spc parsedSpc = new SpcParser().parse(randomSpc);

        assertEquals(randomSpc.length, parsedSpc.getSize());
    }

    @Test(expected = BadSpcException.class)
    public void notSpc() {
        byte[] notSpc = new byte[8 + new Random().nextInt(172)];
        new Random().nextBytes(notSpc);

        new SpcParser().parse(notSpc);
    }

    @Test(expected = BadSpcException.class)
    public void badPaddingSpc() {
        SpcParser spcParser = new SpcParser();

        int payloadLengthPad16 = 16 + new Random().nextInt(2) * 16;
        spcParser.parse(randomSpc(payloadLengthPad16 + 1));
    }

    private static byte[] randomSpc(int payloadLength) {
        int version = 1;
        int reserved = 0;

        byte[] spcDataIv = new byte[SpcFixedLengthField.SPC_DATA_IV.getSize()];
        new Random().nextBytes(spcDataIv);

        byte[] encryptedSpcKey = new byte[SpcFixedLengthField.ENCRYPTED_SPC_KEY.getSize()];
        new Random().nextBytes(encryptedSpcKey);

        byte[] certificateHash = new byte[SpcFixedLengthField.CERTIFICATE_HASH.getSize()];
        new Random().nextBytes(certificateHash);

        byte[] payload = new byte[payloadLength];
        new Random().nextBytes(payload);

        ByteBuffer spcBuffer = ByteBuffer.allocate(
                Integer.BYTES +
                        Integer.BYTES +
                        spcDataIv.length +
                        encryptedSpcKey.length +
                        certificateHash.length +
                        Integer.BYTES +
                        payloadLength
        );

        spcBuffer.putInt(version);
        spcBuffer.putInt(reserved);
        spcBuffer.put(spcDataIv);
        spcBuffer.put(encryptedSpcKey);
        spcBuffer.put(certificateHash);
        spcBuffer.putInt(payloadLength);
        spcBuffer.put(payload);

        return spcBuffer.array();
    }
}
