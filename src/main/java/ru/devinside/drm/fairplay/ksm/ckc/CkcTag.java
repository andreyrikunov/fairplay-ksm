package ru.devinside.drm.fairplay.ksm.ckc;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum CkcTag {
    // A 16-byte encryption of the content key provided by the server.
    ENCRYPTED_CK(0x58b38165af0e3d5aL),

    // A TLLV block containing the 44-byte R1 value that was sent to the KSM in the SPC payload.
    R1(0xea74c4645d5efee9L),

    // A TLLV that specifies the period of validity of the content key.
    // This TLLV may be present only if the KSM has received an SPC with a Media Playback State TLLV.
    CONTENT_KEY_DURATION(0x47acf6a418cd091aL),

    // An optional TLLV that specifies whether HDCP enforcement is required. The absence the TLLV enforces HDCP Type 0.
    HDCP_ENFORCEMENT(0x2e52f1530d8ddb4aL),

    UNIDENTIFIED_TAG(0);

    private final long tag;

    private final static Map<Long, CkcTag> CkcTagByLongValue = Arrays.stream(CkcTag.values()).collect(
            Collectors.toMap(CkcTag::getTag, Function.identity())
    );

    CkcTag(long tag) {
        this.tag = tag;
    }

    public long getTag() {
        return tag;
    }

    public static CkcTag valueOf(long tag) {
        return CkcTagByLongValue.getOrDefault(tag, UNIDENTIFIED_TAG);
    }
}
