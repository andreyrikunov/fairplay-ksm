package ru.devinside.drm.fairplay.ksm.ckc;

import ru.devinside.drm.fairplay.ksm.common.TllvBlock;

import java.util.Collection;

/**
 * A variable-length set of contiguous TLLV blocks.
 * The CKC payload is AES-128 encrypted.
 */
public class CkcPayload {
    private final CkcDataIv ckcDataIv;

    private final CkcEncryptedCk encryptedCk;

    private final CkcR1 r1;

    private final CkcContentKeyDuration ckcContentKeyDuration;

    // The CKC must return, unchanged, the TLLV blocks that the SPC requested in a tag return request.
    private Collection<TllvBlock> returnRequestBlocks;

    public CkcPayload(CkcDataIv ckcDataIv, CkcEncryptedCk encryptedCk, CkcR1 r1, CkcContentKeyDuration ckcContentKeyDuration, Collection<TllvBlock> returnRequestBlocks) {
        this.ckcDataIv = ckcDataIv;
        this.encryptedCk = encryptedCk;
        this.r1 = r1;
        this.ckcContentKeyDuration = ckcContentKeyDuration;
        this.returnRequestBlocks = returnRequestBlocks;
    }

    public CkcDataIv getCkcDataIv() {
        return ckcDataIv;
    }

    public CkcEncryptedCk getEncryptedCk() {
        return encryptedCk;
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
