package ru.devinside.drm.fairplay.ksm.secret;

import ru.devinside.drm.fairplay.ksm.spc.DerivedApplicationSecretKey;
import ru.devinside.drm.fairplay.ksm.spc.MockDerivedApplicationSecretKey;
import ru.devinside.drm.fairplay.ksm.spc.tags.SpcR2;

public class StubDFunction extends DFunction {
    public final static DFunction STUB = new StubDFunction();

    public StubDFunction() {
        super(ApplicationSecretKey.STUB);
    }

    @Override
    public DerivedApplicationSecretKey derive(SpcR2 spcR2) {
        return new MockDerivedApplicationSecretKey();
    }
}