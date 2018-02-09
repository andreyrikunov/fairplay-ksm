package ru.devinside.drm.fairplay.ksm;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.devinside.drm.fairplay.ksm.common.TllvBlock;
import ru.devinside.drm.fairplay.ksm.secret.StubDFunction;
import ru.devinside.drm.fairplay.ksm.spc.*;
import ru.devinside.drm.fairplay.ksm.spc.tags.SkR1Integrity;
import ru.devinside.drm.fairplay.ksm.spc.tags.SpcR2;
import ru.devinside.drm.fairplay.ksm.spc.tags.SpcSkR1;
import ru.devinside.util.Hexler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

//TODO: cleanup
@RunWith(Parameterized.class)
public class SpcVerificationTest {
    private final static String DEV_PRIVATE_KEY_DER_FILE_PATH =
            SpcVerificationTest.class.getResource("/sdk-2.0.3/credentials/dev_private_key.der").getFile();

    private VerificationData verificationData;

    public SpcVerificationTest(VerificationData verificationData) {
        this.verificationData = verificationData;
    }

    @Parameterized.Parameters
    public static Collection<VerificationData> params() {
        return Arrays.asList(
                new VerificationData(
                        SpcVerificationTest.class.getResource("/sdk-2.0.3/verification/FPS/spc1.bin").getFile(),
                        "93B2DD0355E363729D92A45A45CE8D258B0C08AA651C0964976BF0944D2825F3AC8DDE7ED2314FA0EF3FB45B97A226E8C5366DEFE5F1E12BD7B72198A4A8F2653A0EF0DE8C37A47C3C40F012E15C8B593DF12D4B01603A97357E6AE0A11CA3E3",
                        "AFB46E7BF5F31596C1C676DC15E14DC6",
                        "4F45D85CE26273101A97F33081C1D04A",
                        "2752008E1C11E224E8EB07EEC4A09D17440A6372D5DC2109E550ECAC9860613F8B7A8BE6B45A69832D9E8CE7",
                        "11F7BE612CA95EF5E007CE51896AE4502CA3D8801B",
                        "54A16BE0137EF259AB3E4FC79690825F",
                        "1473E5CC53E1E5D6",
                        "F3C69D1E8CC4275A6D3286D332613E13",
                        "DD7139EAFACEED7CDA9F25DA8AA915EA"
                ),
                /* TODO: fix test case
                new VerificationData(
                        SpcVerificationTest.class.getResource("/sdk-2.0.3/verification/FPS/spc2.bin").getFile(),
                        "792EF7420F479B80567E4F8BE83EAC8BFE0B75514A7CDA90E8EF55ACF40F3C59CDFC7DDD34CB9229733D03CD59BD1A06BA21E93E2A0EF45025B1147CB50344965EA06618350FB45B4AB6F7B5B6D16C3F9A3D9F7C38A4AA97EA32663D6D4AAC11",
                        "A663FF9AA82C5915F24D652489FEBDDE",
                        "AAAABBBBCCCC8230E89ACEFADDDDAAAA",
                        "082519941831C7F20437D06ABFEEA38E33576EE16C84C849C24A63D286EB04E08B510E1D521C1C051E77A6B7",
                        "11F7BE612CA95EF5E007CE51896AE4502CA3D8801B",
                        "11664FE96E8CB836AA2EB064E8656611",
                        "DEADC0DEDEADC0DE",
                        "1B67AC159B5AF4A97062F8416AABC0FA",
                        "DD7139EAFACEED7CDA9F25DA8AA915EA"
                ),
                */
                new VerificationData(
                        SpcVerificationTest.class.getResource("/sdk-2.0.3/verification/FPS/spc3.bin").getFile(),
                        "DE58DA53D7EE173EFDF0D1D3F05104A9CB3A213C7AC5AB2B4C452B9577799F3806A09647A5E5D21674617333E32EC12014743C4E16B6D96A61FCA56BCB696C75E923759535B351B8C627A29C7669C8BF88C140860456EF89F28C0EC6C95AA6C3",
                        "076FE16CCC49DE38312476FD5BDF3FFB",
                        "32C082C9E26273101A97F33081C1D04A",
                        "02225DFE75E8AFE5FB761D00C37CF4258A9A7DBF0A8AEF37269D736137C7227BE04F3042275F95ADF9226D8B",
                        "11F7BE612CA95EF5E007CE51896AE4502CA3D8801B",
                        "54A16BE0137EF259AB3E4FC79690825F",
                        "1473E5CC53E1E5D6",
                        "E0B4F262F76B546D85F622A6FF486CAB",
                        "DD7139EAFACEED7CDA9F25DA8AA915EA"
                )
        );
    }

    private static class VerificationData {
        // Input SPC
        public final String spcFilePath;

        // SPC sanity check values
        public final String skR1Payload;
        public final String sk;
        public final String skR1Iv;
        public final String r1;
        public final String r2;
        public final String skR1Integrity;
        public final String transactionId;
        public final String arSeed;
        public final String spck;

        public VerificationData(
                String spcFilePath, String skR1Payload, String sk, String skR1Iv, String r1,
                String r2, String skR1Integrity, String transactionId, String arSeed,
                String spck
        ) {
            this.spcFilePath = spcFilePath;
            this.skR1Payload = skR1Payload;
            this.sk = sk;
            this.skR1Iv = skR1Iv;
            this.r1 = r1;
            this.r2 = r2;
            this.skR1Integrity = skR1Integrity;
            this.transactionId = transactionId;
            this.arSeed = arSeed;
            this.spck = spck;
        }
    }

    @Test
    public void verification() throws IOException {
        Path path = Paths.get(verificationData.spcFilePath);
        byte[] spc = Files.readAllBytes(path);

        SpcParser spcParser = new SpcParser();
        Spc spcMessage = spcParser.parse(spc);

        SpcSecurityService securityContext = new SpcSecurityService(
                new StubDFunction(),
                Files.readAllBytes(Paths.get(DEV_PRIVATE_KEY_DER_FILE_PATH))
        );

        SpcKey spcKey = securityContext.decryptSpcKey(spcMessage.getEncryptedSpcKey());

        SpcPayload payload = securityContext.decryptPayload(spcMessage, spcKey);

        SpcPayloadReader payloadReader = new SpcPayloadReader(payload);
        SpcTllvIndex tllvContainer = payloadReader.index();

        TllvBlock tllvSkR1 = tllvContainer.find(SpcTag.SK_R1);
        SpcSkR1Raw spcSkR1Raw = new SpcSkR1Raw(tllvSkR1);

        TllvBlock tllvR2 = tllvContainer.find(SpcTag.R2);
        SpcR2 spcR2 = new SpcR2(tllvR2);

        SpcSkR1 spcSkR1 = securityContext.decryptSkR1(spcSkR1Raw, spcR2);

        TllvBlock tllvSkR1Integrity = tllvContainer.find(SpcTag.SK_R1_INTEGRITY);

        assertTrue(securityContext.integrityCheckSkR1(spcSkR1, new SkR1Integrity(tllvSkR1Integrity)));
        assertEquals(verificationData.skR1Payload, Hexler.toHexString(spcSkR1Raw.getPayload()));
        assertEquals(verificationData.sk, Hexler.toHexString(spcSkR1.getSessionKey()));
        assertEquals(verificationData.skR1Iv, Hexler.toHexString(spcSkR1Raw.getIv()));
        assertEquals(verificationData.r1, Hexler.toHexString(spcSkR1.getR1()));
        assertEquals(verificationData.skR1Integrity, Hexler.toHexString(spcSkR1.getIntegrity()));
        assertEquals(verificationData.skR1Integrity, Hexler.toHexString(tllvSkR1Integrity.getValue()));
        assertEquals(verificationData.r2, Hexler.toHexString(spcR2.getR2()));
        assertEquals(verificationData.transactionId, Hexler.toHexString(tllvContainer.find(SpcTag.TRANSACTION_ID).getValue()));
        assertEquals(verificationData.arSeed, Hexler.toHexString(tllvContainer.find(SpcTag.AR_SEED).getValue()));
        assertEquals(verificationData.spck, Hexler.toHexString(spcKey.getSpck()));

        // assert return tags
    }
}
