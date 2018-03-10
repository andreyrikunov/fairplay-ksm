package ru.devinside.drm.fairplay.ksm.spc;

import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;
import ru.devinside.drm.fairplay.ksm.common.TllvBlock;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TllvBlockIndexTest {
    @Test(expected = BadSpcException.class)
    public void noDuplicatesAllowed() throws IOException {
        long tag = RandomUtils.nextLong();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(randomTllvBlock(tag).asBytes());
        out.write(randomTllvBlock(tag).asBytes());

        TllvBlockIndex.build(
                new SpcPayloadReader(new SpcPayload(out.toByteArray()))
        );
    }

    private TllvBlock randomTllvBlock(long tag) {
        return new TllvBlock(
                tag,
                RandomUtils.nextBytes(32),
                32,
                new byte[] {} // no padding
        );
    }
}
