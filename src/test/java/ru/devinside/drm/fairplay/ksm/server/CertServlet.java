package ru.devinside.drm.fairplay.ksm.server;

import ru.devinside.drm.fairplay.ksm.secret.FpsCertificate;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ru.devinside.drm.fairplay.ksm.TestUtils.readResourceBytes;

public class CertServlet extends HttpServlet {
    private final FpsCertificate FPS_CERT = new FpsCertificate(readResourceBytes("/secrets/fairplay.cer"));

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        FPS_CERT.write(resp.getOutputStream());
    }
}