package ru.devinside.drm.fairplay.ksm;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.devinside.drm.fairplay.ksm.ckc.CkcDataIv;
import ru.devinside.drm.fairplay.ksm.ckc.CkcEncryptedPayload;
import ru.devinside.drm.fairplay.ksm.ckc.CkcMessage;
import ru.devinside.drm.fairplay.ksm.ckc.CkcMessageSerializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class CkcVerificationTest {
    private VerificationData verificationData;

    public CkcVerificationTest(VerificationData verificationData) {
        this.verificationData = verificationData;
    }

    @Parameterized.Parameters
    public static Collection<VerificationData> params() {
        return Arrays.asList(
                new VerificationData(
                        SpcVerificationTest.class.getResource("/verification/fps/spc1.bin").getFile(),
                        "AFB46E7BF5F31596C1C676DC15E14DC6",
                        "4F45D85CE26273101A97F33081C1D04A"
                ),
                new VerificationData(
                        SpcVerificationTest.class.getResource("/verification/fps/spc2.bin").getFile(),
                        "A663FF9AA82C5915F24D652489FEBDDE",
                        "AAAABBBBCCCC8230E89ACEFADDDDAAAA"
                ),
                new VerificationData(
                        SpcVerificationTest.class.getResource("/verification/fps/spc3.bin").getFile(),
                        "076FE16CCC49DE38312476FD5BDF3FFB",
                        "32C082C9E26273101A97F33081C1D04A"
                )
        );
    }

    private static class VerificationData {
        // Input SPC
        public final String spcFilePath;

        // TODO: add more sanity checks
        // CKC sanity check values
        public final String spcSkValue;
        public final String spcSkR1IvValue;

        public VerificationData(String spcFilePath, String spcSkValue, String spcSkR1IvValue) {
            this.spcFilePath = spcFilePath;
            this.spcSkValue = spcSkValue;
            this.spcSkR1IvValue = spcSkR1IvValue;
        }
    }

    @Test
    public void verification() throws IOException {
        CkcDataIv ckcDataIv = CkcDataIv.generate();
        CkcMessage ckcMessage = new CkcMessage(ckcDataIv, new CkcEncryptedPayload(new byte[0]));

        CkcMessageSerializer ckcMessageSerializer = new CkcMessageSerializer(ckcMessage);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ckcMessageSerializer.serializeTo(out);

        assertTrue(out.toByteArray().length > 0);
    }
}
