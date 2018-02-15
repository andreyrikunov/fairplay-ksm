package ru.devinside.drm.fairplay.ksm.spc;

import ru.devinside.drm.fairplay.ksm.common.TllvBlock;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class ProtocolVersionsSupportedParser {
    public final static ProtocolVersionsSupportedParser INSTANCE = new ProtocolVersionsSupportedParser();

    public Collection<Integer> parse(TllvBlock tllvBlock) {
        if(tllvBlock.getTag() != SpcTag.PROTOCOL_VERSIONS_SUPPORTED.getTag()) {
            throw new IllegalArgumentException(
                    String.format(
                            "Wrong TLLV block passed %s but %s required",
                            tllvBlock.getTag(),
                            SpcTag.PROTOCOL_VERSIONS_SUPPORTED.getTag()
                    )
            );
        }

        boolean blockIsCorrectlyAligned = (tllvBlock.getValue().length % 4) == 0;

        if(!blockIsCorrectlyAligned) {
            throw new IllegalArgumentException(
                    String.format(
                            "The %s TLLV block value is not correctly aligned. Must be multiple of 4 bytes",
                            SpcTag.PROTOCOL_VERSIONS_SUPPORTED
                    )
            );
        }

        int versionsNum = tllvBlock.getValue().length / 4;
        Collection<Integer> protocolVersionsSupported = new ArrayList<>(versionsNum);
        ByteBuffer byteBuffer = ByteBuffer.wrap(tllvBlock.getValue());
        for (int i = 0; i < versionsNum; i++) {
             protocolVersionsSupported.add(byteBuffer.getInt());
        }

        return Collections.unmodifiableCollection(protocolVersionsSupported);
    }
}
