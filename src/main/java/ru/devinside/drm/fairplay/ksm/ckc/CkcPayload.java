package ru.devinside.drm.fairplay.ksm.ckc;

import org.apache.commons.lang3.RandomUtils;
import ru.devinside.drm.fairplay.ksm.common.TllvBlock;

import java.nio.ByteBuffer;
import java.util.Collection;

/**
 * A variable-length set of contiguous TLLV blocks.
 * The CKC payload is AES-128 encrypted.
 */
public class CkcPayload {
    private final CkcEncryptedCk encryptedCk;

    private final ContentKeyIv contentKeyIv;

    private final CkcR1 r1;

    private final CkcContentKeyDuration ckcContentKeyDuration;

    // The CKC must return, unchanged, the TLLV blocks that the SPC requested in a tag return request.
    private Collection<TllvBlock> returnRequestBlocks;

    // TODO: add HDCP enforcement

    public CkcPayload(
            CkcEncryptedCk encryptedCk,
            ContentKeyIv contentKeyIv,
            CkcR1 r1,
            CkcContentKeyDuration ckcContentKeyDuration,
            Collection<TllvBlock> returnRequestBlocks
    ) {
        this.encryptedCk = encryptedCk;
        this.contentKeyIv = contentKeyIv;
        this.r1 = r1;
        this.ckcContentKeyDuration = ckcContentKeyDuration;
        this.returnRequestBlocks = returnRequestBlocks;
    }

    public CkcEncryptedCk getEncryptedCk() {
        return encryptedCk;
    }

    public ContentKeyIv getContentKeyIv() {
        return contentKeyIv;
    }

    public CkcR1 getR1() {
        return r1;
    }

    public CkcContentKeyDuration getCkcContentKeyDuration() {
        return ckcContentKeyDuration;
    }

    public Collection<TllvBlock> getReturnRequestBlocks() {
        return returnRequestBlocks;
    }

    public byte[] getBytes() {
        byte[] eck = getEncryptedCk().getEncryptedCk();

        TllvBlock r1 = new TllvBlock(CkcTag.R1.getTag(), getR1().getR1());

        int rrLen = getReturnRequestBlocks().stream().mapToInt(TllvBlock::length).sum();

        ByteBuffer out = ByteBuffer.allocate(
                8 + 4 + 4 + ContentKeyIv.CONTENT_KEY_DATA_IV_SIZE + eck.length + 16 + r1.length() + rrLen
        );

        out.putLong(CkcTag.ENCRYPTED_CK.getTag());
        out.putInt(48); // Block length: Value(32) + Padding(16)
        out.putInt(32); // Value length: IV(16) + CK(16)
        out.put(getContentKeyIv().getIv());
        out.put(eck);
        out.put(RandomUtils.nextBytes(16));
        out.put(r1.asBytes());
        // TODO: out.put(getCkcContentKeyDuration().asBytes());

        for (TllvBlock tllvBlock : getReturnRequestBlocks()) {
            out.put(tllvBlock.asBytes());
        }

        return out.array();
    }
}
