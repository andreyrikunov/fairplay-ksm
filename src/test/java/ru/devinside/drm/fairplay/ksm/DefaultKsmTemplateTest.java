package ru.devinside.drm.fairplay.ksm;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.PumpStreamHandler;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.devinside.drm.fairplay.ksm.secret.ContentKey;
import ru.devinside.drm.fairplay.ksm.secret.FpsCertificate;
import ru.devinside.drm.fairplay.ksm.secret.StubDFunction;
import ru.devinside.drm.fairplay.ksm.spc.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertTrue;
import static ru.devinside.drm.fairplay.ksm.TestUtils.getResourcePath;
import static ru.devinside.drm.fairplay.ksm.TestUtils.readResourceBytes;

@RunWith(Parameterized.class)
public class DefaultKsmTemplateTest {
    private final static String FPS_CERT_FILE_PATH = "/sdk-4.1.0/credentials/dev_certificate.der";
    private final static String DEV_PRIVATE_KEY_DER_FILE_PATH = "/sdk-4.1.0/credentials/dev_private_key.der";
    private final static String VERIFY_CKC_CMD_PATH = getResourcePath("/sdk-4.1.0/verification/tool/verify_ckc");

    private String spcPath;

    @Before
    public void macOsOnly() {
        Assume.assumeTrue(System.getProperty("os.name").toLowerCase().contains("mac"));
    }

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder();

    public DefaultKsmTemplateTest(String spcPath) {
        this.spcPath = spcPath;
    }

    @Parameterized.Parameters
    public static Collection<String> params() {
        return Arrays.asList(
                "/sdk-4.1.0/verification/FPS/spc1.bin",
                "/sdk-4.1.0/verification/FPS/spc2.bin",
                "/sdk-4.1.0/verification/FPS/spc3.bin"
        );
    }

    @Test
    public void verification() throws IOException {
        SpcSecurityService securityContext = new SpcSecurityService(
                new FpsCertificate(readResourceBytes(FPS_CERT_FILE_PATH)),
                new StubDFunction(),
                readResourceBytes(DEV_PRIVATE_KEY_DER_FILE_PATH)
        );

        KsmTemplate ksmTemplate = new DefaultKsmTemplate(securityContext);

        SpcParser spcParser = new SpcParser();
        Spc spc = spcParser.parse(readResourceBytes(spcPath));
        byte[] ckc = ksmTemplate.compute(
                spc,
                ctx -> new ContentKey() {
                    @Override
                    public byte[] getKey() {
                        return new byte[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
                    }

                    @Override
                    public byte[] getIv() {
                        return new byte[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
                    }
                },
                clientServerProtocolCompatibility -> {}
        ).getBytes();

        assertTrue(ckc.length > 0);

        File ckcFile = tmp.newFile();
        Files.write(ckcFile.toPath(), ckc, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        verifyCkc(ckcFile.getAbsolutePath());
    }

    private void verifyCkc(String ckcPath) throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        String cmd = String.format("%s -v -s %s -c %s", VERIFY_CKC_CMD_PATH, getResourcePath(spcPath), ckcPath);
        CommandLine cmdLine = CommandLine.parse(cmd);
        DefaultExecutor executor = new DefaultExecutor();
        executor.setStreamHandler(new PumpStreamHandler(out));
        try {
            executor.execute(cmdLine);
        } catch (ExecuteException e) {
            throw new RuntimeException(out.toString(), e);
        }
        System.out.println(out.toString());
    }
}
