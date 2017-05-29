package ru.devinside.drm.fairplay.ksm.secret;

import ru.devinside.drm.fairplay.ksm.spc.DerivedApplicationSecretKey;
import ru.devinside.drm.fairplay.ksm.spc.tags.SpcR2;

/**
 * Apple provides
 */
public class DFunction {
    public DerivedApplicationSecretKey compute(SpcR2 r2, ApplicationSecretKey ask) {
        return new DerivedApplicationSecretKey();
    }
}
