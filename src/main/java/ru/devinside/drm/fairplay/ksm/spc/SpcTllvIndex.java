package ru.devinside.drm.fairplay.ksm.spc;

import ru.devinside.drm.fairplay.ksm.common.TllvBlock;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Holds all SPC TLLV blocks indexed by tag
 *
 * TODO: Each tag must be present once only.
 */
public class SpcTllvIndex {
    private final Map<Long, TllvBlock> tllvRawByTag = new HashMap<>();

    public void add(TllvBlock tllvRaw) {
        tllvRawByTag.put(tllvRaw.getTag(), tllvRaw);
    }

    public TllvBlock find(long tag) {
        return tllvRawByTag.get(tag);
    }

    public TllvBlock find(SpcTag spcTag) {
        return find(spcTag.getTag());
    }

    public Collection<TllvBlock> findUndefined() {
        return tllvRawByTag.values().stream().filter(
                tllv -> SpcTag.valueOf(tllv.getTag()) == SpcTag.UNIDENTIFIED_TAG
        ).collect(Collectors.toList());
    }

    public Collection<TllvBlock> findReturnRequestBlocks() {
        List<TllvBlock> returnRequestBlocks = new ArrayList<>();

        TllvBlock returnRequest = find(SpcTag.RETURN_REQUEST);
        ByteBuffer buffer = ByteBuffer.wrap(returnRequest.getValue());
        while(buffer.hasRemaining()) {
            returnRequestBlocks.add(find(buffer.getLong()));
        }

        return returnRequestBlocks;
    }
}
