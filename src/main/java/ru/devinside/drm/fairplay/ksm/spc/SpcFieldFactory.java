package ru.devinside.drm.fairplay.ksm.spc;

import ru.devinside.drm.fairplay.ksm.common.BinVal;
import ru.devinside.drm.fairplay.ksm.common.BinValFactory;

import java.nio.ByteBuffer;

public class SpcFieldFactory {
    public static BinVal from(ByteBuffer buffer, SpcFixedLengthField field) {
        return BinValFactory.fixedLength(buffer, field.getSize());
    }

    public static BinVal from(ByteBuffer buffer, SpcVariableLengthField field) {
        return BinValFactory.variableLength(buffer, field.getPadding());
    }
}
