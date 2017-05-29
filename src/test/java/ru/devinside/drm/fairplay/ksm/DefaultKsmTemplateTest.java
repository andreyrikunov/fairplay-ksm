package ru.devinside.drm.fairplay.ksm;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.devinside.drm.fairplay.ksm.spc.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class DefaultKsmTemplateTest {
    private final static String DEV_PRIVATE_KEY_DER_FILE_PATH =
            SpcVerificationTest.class.getResource("/credentials/dev_private_key.der").getFile();

    private String spcVerificationFilePath;

    public DefaultKsmTemplateTest(String spcVerificationFilePath) {
        this.spcVerificationFilePath = spcVerificationFilePath;
    }

    @Parameterized.Parameters
    public static Collection<String> params() {
        return Arrays.asList(
                SpcVerificationTest.class.getResource("/verification/fps/spc1.bin").getFile(),
                SpcVerificationTest.class.getResource("/verification/fps/spc2.bin").getFile(),
                SpcVerificationTest.class.getResource("/verification/fps/spc3.bin").getFile()
        );
    }

    @Test
    public void basicUsage() throws IOException {
        SpcSecurityContext securityContext = new SpcSecurityContext(
                Files.readAllBytes(Paths.get(DEV_PRIVATE_KEY_DER_FILE_PATH))
        );

        KsmTemplate ksmTemplate = new DefaultKsmTemplate(securityContext);
        byte[] ckc = ksmTemplate.generateCkc(Files.readAllBytes(Paths.get(spcVerificationFilePath)));

        assertTrue(ckc.length > 0);

        // TODO: move verification to test
        Files.write(
                Paths.get(SpcVerificationTest.class.getResource("/verification/tool/").getFile() + Paths.get(spcVerificationFilePath).getFileName() + ".ckc"),
                ckc,
                StandardOpenOption.CREATE, StandardOpenOption.WRITE
        );
    }
}
