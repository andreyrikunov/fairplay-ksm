package ru.devinside.drm.fairplay.ksm.server;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.time.StopWatch;
import ru.devinside.drm.fairplay.ksm.DefaultKsmTemplate;
import ru.devinside.drm.fairplay.ksm.KsmTemplate;
import ru.devinside.drm.fairplay.ksm.secret.ApplicationSecretKey;
import ru.devinside.drm.fairplay.ksm.secret.ContentKey;
import ru.devinside.drm.fairplay.ksm.secret.DFunction;
import ru.devinside.drm.fairplay.ksm.secret.FpsCertificate;
import ru.devinside.drm.fairplay.ksm.spc.Spc;
import ru.devinside.drm.fairplay.ksm.spc.SpcParser;
import ru.devinside.drm.fairplay.ksm.spc.SpcSecurityService;
import ru.devinside.util.Hexler;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

import static ru.devinside.drm.fairplay.ksm.TestUtils.readResourceBytes;

public class KeyServlet extends HttpServlet {
    private final static String ASK_CLASSPATH = "/secrets/ask.bin";
    private final static String PRIVATE_KEY_DER_CLASSPATH = "/secrets/privatekey.der";

    private final FpsCertificate FPS_CERT = new FpsCertificate(readResourceBytes("/secrets/fairplay.cer"));
    
    // gear
//    private final static String CONTENT_KEY = "3c3c3c3c3c3c3c3c3c3c3c3c3c3c3c3c";
//    private final static String CONTENT_KEY_IV = "d5fbd6b82ed93e4ef98ae40931ee33b7";

    // big buck bunny
    private final static String CONTENT_KEY = "00112233445566778899aabbccddeeff";
    private final static String CONTENT_KEY_IV = "112233445566778899aabbccddeeff00";

    private final static ContentKey STATIC_CONTENT_KEY = new ContentKey() {
        @Override
        public byte[] getKey() {
            return Hexler.toByteArray(CONTENT_KEY);
        }

        @Override
        public byte[] getIv() {
            return Hexler.toByteArray(CONTENT_KEY_IV);
        }
    };

    private final SpcParser spcParser = new SpcParser();
    private KsmTemplate ksmTemplate;

    @Override
    public void init(ServletConfig config) {
        SpcSecurityService securityService = new SpcSecurityService(
                FPS_CERT,
                new DFunction(
                        new ApplicationSecretKey(readResourceBytes(ASK_CLASSPATH))
                ),
                readResourceBytes(PRIVATE_KEY_DER_CLASSPATH)
        );

        ksmTemplate = new DefaultKsmTemplate(securityService);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Writer writer = resp.getWriter();
        writer.write("Use POST to submit SCP!");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Spc spc = spcParser.parse(IOUtils.toByteArray(req.getInputStream()));

        System.out.println("SPC length: " + spc.getSize());

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        byte[] ckc = ksmTemplate.compute(
                spc,
                ctx -> STATIC_CONTENT_KEY,
                clientServerProtocolCompatibility -> {}
        ).getBytes();

        stopWatch.stop();

        System.out.println(String.format("KSM spent: %s ms", stopWatch.getTime(TimeUnit.MILLISECONDS)));

        Writer writer = resp.getWriter();
        writer.write(String.format("<ckc>%s</ckc>", Base64.getEncoder().encodeToString(ckc)));
    }
}