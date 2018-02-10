package ru.devinside.drm.fairplay.ksm.server;

import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ru.devinside.drm.fairplay.ksm.TestUtils.readResourceBytes;

public class CertServlet extends HttpServlet {
    private final byte[] CERT = readResourceBytes("/secrets/fairplay.cer");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        IOUtils.write(CERT, resp.getOutputStream());
    }
}