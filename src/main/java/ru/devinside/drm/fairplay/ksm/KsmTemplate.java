package ru.devinside.drm.fairplay.ksm;

import ru.devinside.drm.fairplay.ksm.ckc.Ckc;
import ru.devinside.drm.fairplay.ksm.secret.ContentKey;
import ru.devinside.drm.fairplay.ksm.spc.AssetId;
import ru.devinside.drm.fairplay.ksm.spc.Spc;

import java.util.function.Function;

public interface KsmTemplate {
    Ckc process(Spc spc, Function<AssetId, ContentKey> keyFunction);
}
