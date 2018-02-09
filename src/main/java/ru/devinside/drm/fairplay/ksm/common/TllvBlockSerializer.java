package ru.devinside.drm.fairplay.ksm.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class TllvBlockSerializer {
    private final TllvBlock tllvBlock;

    public TllvBlockSerializer(TllvBlock tllvBlock) {
        this.tllvBlock = tllvBlock;
    }

    // TODO: replace with ByteBuffer
    public byte[] serialize() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(ByteBuffer.allocate(8).putLong(tllvBlock.getTag()).array());
        out.write(ByteBuffer.allocate(4).putInt(tllvBlock.getTotalBlockLength()).array());
        out.write(ByteBuffer.allocate(4).putInt(tllvBlock.getValue().length).array());
        out.write(tllvBlock.getValue());
        out.write(tllvBlock.getPadding());
        return out.toByteArray();
    }
}
