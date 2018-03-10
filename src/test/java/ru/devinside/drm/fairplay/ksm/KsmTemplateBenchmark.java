package ru.devinside.drm.fairplay.ksm;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import ru.devinside.drm.fairplay.ksm.ckc.Ckc;
import ru.devinside.drm.fairplay.ksm.secret.ContentKey;
import ru.devinside.drm.fairplay.ksm.secret.FpsCertificate;
import ru.devinside.drm.fairplay.ksm.secret.StubDFunction;
import ru.devinside.drm.fairplay.ksm.spc.SpcParser;
import ru.devinside.drm.fairplay.ksm.spc.SpcSecurityService;

import java.util.Random;

import static ru.devinside.drm.fairplay.ksm.TestUtils.readResourceBytes;

@State(Scope.Benchmark)
public class KsmTemplateBenchmark {
    private final static String FPS_CERT_FILE_PATH = "/sdk-4.1.0/credentials/dev_certificate.der";
    private final static String DEV_PRIVATE_KEY_DER_FILE_PATH = "/sdk-4.1.0/credentials/dev_private_key.der";

    private static byte[] SPC1 = readResourceBytes("/sdk-4.1.0/verification/FPS/spc1.bin");
    private static byte[] SPC2 = readResourceBytes("/sdk-4.1.0/verification/FPS/spc2.bin");
    private static byte[] SPC3 = readResourceBytes("/sdk-4.1.0/verification/FPS/spc3.bin");
    private static byte[][] SPC = new byte[][] { SPC1, SPC2, SPC3 };

    private static SpcParser SPC_PARSER = new SpcParser();

    private final static Random RND = new Random();

    private final static ContentKey KEY = new ContentKey() {
        @Override
        public byte[] getKey() {
            return new byte[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 0, 0, 0, 0, 0};
        }

        @Override
        public byte[] getIv() {
            return new byte[] {0, 0, 0, 0, 0, 0, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0};
        }
    };

    private final KsmTemplate ksm;

    public KsmTemplateBenchmark() {
        SpcSecurityService securityContext = new SpcSecurityService(
                new FpsCertificate(readResourceBytes(FPS_CERT_FILE_PATH)),
                new StubDFunction(),
                readResourceBytes(DEV_PRIVATE_KEY_DER_FILE_PATH)
        );

        ksm = new DefaultKsmTemplate(securityContext);
    }

    @Warmup(iterations = 1)
    @Measurement(iterations = 3)
    @Benchmark @BenchmarkMode({Mode.Throughput, Mode.AverageTime})
    public void benchmark() {
        Ckc ckc = ksm.compute(SPC_PARSER.parse(SPC[RND.nextInt(3)]), ctx -> KEY, compatibility -> {});
        byte[] ckcBytes = ckc.getBytes();
        System.out.println(ckcBytes.length); // Simulate I/O :)
    }

    public static void main(String... args) throws RunnerException {
        new Runner(
                new OptionsBuilder()
                        .include(".*")
                        .jvmArgs("-Xms1g", "-Xmx1g")
                        .shouldDoGC(true)
                        .forks(1)
                        .build()
        ).run();
    }
}
