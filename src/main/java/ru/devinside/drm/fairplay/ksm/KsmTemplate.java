package ru.devinside.drm.fairplay.ksm;

import ru.devinside.drm.fairplay.ksm.ckc.Ckc;
import ru.devinside.drm.fairplay.ksm.secret.ContentKey;
import ru.devinside.drm.fairplay.ksm.spc.AssetId;
import ru.devinside.drm.fairplay.ksm.spc.ClientServerProtocolCompatibility;
import ru.devinside.drm.fairplay.ksm.spc.Spc;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;

public interface KsmTemplate {
    Ckc process(
            Spc spc,
            Function<? super AssetId, ? extends ContentKey> contentKeyCallback,
            Consumer<? super ClientServerProtocolCompatibility> compatibilityCallback
    );

    Collection<Integer> protocolVersions();
}
