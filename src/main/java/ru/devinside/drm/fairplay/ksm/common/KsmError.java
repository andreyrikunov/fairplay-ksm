package ru.devinside.drm.fairplay.ksm.common;

public enum KsmError {
    PLAYBACK_CONTEXT_VERSION_ERR(-42580);

    int errorCode;

    KsmError(int errorCode) {
        this.errorCode = errorCode;
    }

//    ParserErr                 = -42581,
//    CKCGenErr                 = -42582,
//    MissingRequiredTag        = -42583,
//    CKNotFound                = -42584,
//    ParamErr                  = -42585,
//    MemErr                    = -42586,
//    FileNotFoundErr           = -42587,
//    OpenSSLErr                = -42588,
//    IntegrityErr              = -42589,
//    VersionErr                = -42590,
//    DupTagErr                 = -42591,
}
