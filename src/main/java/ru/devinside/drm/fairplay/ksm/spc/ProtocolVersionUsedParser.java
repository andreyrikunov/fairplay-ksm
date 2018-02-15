package ru.devinside.drm.fairplay.ksm.spc;

import ru.devinside.drm.fairplay.ksm.common.TllvBlock;

import java.nio.ByteBuffer;

public class ProtocolVersionUsedParser {
    public final static ProtocolVersionUsedParser INSTANCE = new ProtocolVersionUsedParser();

    public Integer parse(TllvBlock tllvBlock) {
        if(tllvBlock.getTag() != SpcTag.PROTOCOL_VERSION_USED.getTag()) {
            throw new IllegalArgumentException(
                    String.format(
                            "Wrong TLLV block passed %s but %s required",
                            tllvBlock.getTag(),
                            SpcTag.PROTOCOL_VERSION_USED.getTag()
                    )
            );
        }

        if(tllvBlock.getValue().length != 4) {
            throw new IllegalArgumentException(
                    String.format(
                            "The %s TLLV block value length is not correct. Must be of 4 bytes but %s bytes passed",
                            SpcTag.PROTOCOL_VERSION_USED,
                            tllvBlock.getValue().length
                    )
            );
        }

        return ByteBuffer.wrap(tllvBlock.getValue()).getInt();
    }
}
