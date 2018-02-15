package ru.devinside.drm.fairplay.ksm.spc;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.devinside.drm.fairplay.ksm.TestUtils;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class SpcParserVerificationTest {
    private SpcVerificationData spcVerificationData;

    public SpcParserVerificationTest(SpcVerificationData spcVerificationData) {
        this.spcVerificationData = spcVerificationData;
    }

    @Parameterized.Parameters
    public static Collection<SpcVerificationData> params() {
        return Arrays.asList(
                new SpcVerificationData(
                        TestUtils.readResourceBytes("/sdk-2.0.3/verification/FPS/spc1.bin"),
                        1,
                        "5D1644EAEC11F983147541E46EEB2774",
                        "926648B9861EC0471BA21758851C3DDA31C93B1DD601AA4EAD4415A20759AAB9" +
                                "A6D89F551385856E73571729DF2F1D46D25C13DA2AD75D00FD3413EBD96CA47D" +
                                "02955C569F7FAB40F1A7FB23414167A653EABDF1AD283DF5E07E7CF4AA2FBAC6" +
                                "4F1D460FDF9A21EEB27A7F60727853A414C1C450C525E8DAB6A3F13CFA57171A",
                        "5561980AA51F03AA04BCA24556C841A71736C771",
                        "6DA6DAE4",
                        2688

                ),
                new SpcVerificationData(
                        TestUtils.readResourceBytes("/sdk-4.1.0/verification/FPS/spc2.bin"),
                        1,
                        "8888C5A942130323AA4C9F1DA77C8888",
                        "1F603470489DDE55FBD21790662905F9FC7CF04194F649A7551E19D7F2145BFE" +
                                "CB8A07F941CCB89ACEC0DC7C0F082C8CD55560ADB56AAE1CD96EB2444341EBBE" +
                                "84518A931930556A881FE250F57D2197EA239A79A83CB47DC8461E4BE5EBEEC7" +
                                "4B4E4079A97729D9A56DCD9F2054C33FE0ACEB6177EA9E790D1AED6287BF8B0B",
                        "5561980AA51F03AA04BCA24556C841A71736C771",
                        "615CA9AA",
                        1984

                )
        );
    }

    private static class SpcVerificationData {
        private final byte[] spc;

        private final int version;
        private final String spcDataIv;
        private final String encryptedSpcKey;
        private final String certificateHash;
        private final String encryptedPayload4Bytes;
        private final int encryptedPayloadLength;

        SpcVerificationData(
                byte[] spc,
                int version,
                String spcDataIv,
                String encryptedSpcKey,
                String certificateHash,
                String encryptedPayload4Bytes,
                int encryptedPayloadLength
        ) {
            this.spc = spc;
            this.version = version;
            this.spcDataIv = spcDataIv;
            this.encryptedSpcKey = encryptedSpcKey;
            this.certificateHash = certificateHash;
            this.encryptedPayload4Bytes = encryptedPayload4Bytes;
            this.encryptedPayloadLength = encryptedPayloadLength;
        }
    }

    @Test
    public void verify() {
        SpcParser spcParser = new SpcParser();
        Spc spc = spcParser.parse(spcVerificationData.spc);

        assertEquals(spcVerificationData.version, spc.getVersion());
        assertEquals(spcVerificationData.spcDataIv, spc.getSpcDataIv().toString());
        assertEquals(spcVerificationData.encryptedSpcKey, spc.getEncryptedSpcKey().toString());
        assertEquals(spcVerificationData.certificateHash, spc.getCertificateHash().toString());
        assertEquals(
                spcVerificationData.encryptedPayload4Bytes,
                spc.getEncryptedPayload().toString().substring(0, 8)
        );
        assertEquals(spcVerificationData.encryptedPayloadLength, spc.getEncryptedPayload().length());
    }
}
