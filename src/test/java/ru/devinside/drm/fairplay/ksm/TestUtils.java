package ru.devinside.drm.fairplay.ksm;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestUtils {
    public static byte[] readResourceBytes(String path) {
        try {
            return Files.readAllBytes(Paths.get(getResourcePath(path)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getResourcePath(String path) {
        return TestUtils.class.getResource(path).getFile();
    }
}
