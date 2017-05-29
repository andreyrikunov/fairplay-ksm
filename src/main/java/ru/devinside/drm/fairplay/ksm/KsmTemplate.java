package ru.devinside.drm.fairplay.ksm;

import java.io.IOException;

public interface KsmTemplate {
    byte[] generateCkc(byte[] spc) throws IOException;
}
