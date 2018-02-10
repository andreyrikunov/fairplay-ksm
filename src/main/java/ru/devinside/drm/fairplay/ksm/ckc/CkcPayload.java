package ru.devinside.drm.fairplay.ksm.ckc;

import ru.devinside.drm.fairplay.ksm.common.TllvBlock;

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
}
