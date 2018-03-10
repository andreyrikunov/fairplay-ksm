package ru.devinside.drm.fairplay.ksm.spc;

import ru.devinside.drm.fairplay.ksm.common.TllvBlock;

import java.nio.ByteBuffer;
import java.util.*;

/**
 * Holds all SPC TLLV blocks indexed by tag
 */
public class TllvBlockIndex {
    private final Map<Long, TllvBlock> tllvBlockByTag = new HashMap<>();

    private TllvBlockIndex() {
    }

    public static TllvBlockIndex build(SpcPayloadReader reader) {
        TllvBlockIndex index = new TllvBlockIndex();
        while(reader.hasNext()) {
            index.add(reader.next());
        }
        return index;
    }

    private void add(TllvBlock tllvBlock) {
        tllvBlockByTag.merge(
                tllvBlock.getTag(),
                tllvBlock,
                (oldValue, value) -> {
                    throw new BadSpcException("SPC must contain unique TLLV blocks only");
                }
        );
    }

    public TllvBlock find(long tag) {
        return tllvBlockByTag.get(tag);
    }

    public TllvBlock find(SpcTag spcTag) {
        return find(spcTag.getTag());
    }

    public Collection<TllvBlock> findReturnRequestBlocks() {
        List<TllvBlock> returnRequestTllvBlocks = new ArrayList<>();
        TllvBlock returnRequest = find(SpcTag.RETURN_REQUEST);
        ByteBuffer buffer = ByteBuffer.wrap(returnRequest.getValue());
        while(buffer.hasRemaining()) {
            returnRequestTllvBlocks.add(
                    find(buffer.getLong())
            );
        }
        return returnRequestTllvBlocks;
    }
}
