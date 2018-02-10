package ru.devinside.drm.fairplay.ksm;

import ru.devinside.drm.fairplay.ksm.ckc.*;
import ru.devinside.drm.fairplay.ksm.common.BinVal;
import ru.devinside.drm.fairplay.ksm.common.TllvBlock;
import ru.devinside.drm.fairplay.ksm.secret.ContentKey;
import ru.devinside.drm.fairplay.ksm.spc.*;
import ru.devinside.drm.fairplay.ksm.spc.tags.SkR1Integrity;
import ru.devinside.drm.fairplay.ksm.spc.tags.SpcR2;
import ru.devinside.drm.fairplay.ksm.spc.tags.SpcSkR1;

import java.util.function.Function;

public class DefaultKsmTemplate implements KsmTemplate {
    private final SpcSecurityService security;

    public DefaultKsmTemplate(SpcSecurityService security) {
        this.security = security;
    }

    @Override
    public Ckc process(Spc spc, Function<AssetId, ContentKey> keyFunction) {
        SpcKey spcKey = security.decryptSpcKey(spc.getEncryptedSpcKey());
        SpcPayload payload = security.decryptPayload(spc, spcKey);

        SpcTllvIndex tllvContainer = new SpcPayloadReader(payload).index();

        TllvBlock protocolVersionSupported = tllvContainer.find(SpcTag.PROTOCOL_VERSIONS_SUPPORTED);
        TllvBlock protocolVersionUsed = tllvContainer.find(SpcTag.PROTOCOL_VERSION_USED);

        // TODO: check versioning/compatibility of device and ksm

        TllvBlock tllvSkR1 = tllvContainer.find(SpcTag.SK_R1);
        SpcSkR1Raw spcSkR1Raw = new SpcSkR1Raw(tllvSkR1);

        TllvBlock tllvR2 = tllvContainer.find(SpcTag.R2);
        SpcR2 spcR2 = new SpcR2(tllvR2);

        SpcSkR1 spcSkR1 = security.decryptSkR1(spcSkR1Raw, spcR2);

        TllvBlock tllvSkR1Integrity = tllvContainer.find(SpcTag.SK_R1_INTEGRITY);

        if(!security.integrityCheckSkR1(spcSkR1, new SkR1Integrity(tllvSkR1Integrity))) {
            throw new IllegalArgumentException("Bad SPC message");
        }

        AssetId assetId = new AssetId(new BinVal(tllvContainer.find(SpcTag.ASSET_ID).getValue()));

        return generateCkc(spcSkR1, tllvContainer, keyFunction.apply(assetId));
    }

    private Ckc generateCkc(SpcSkR1 spcSkR1, SpcTllvIndex tllvContainer, ContentKey ck) {
        CkcSecurityContext ckcSecurityContext = new CkcSecurityContext(
                spcSkR1,
                new SpcArSeed(tllvContainer.find(SpcTag.AR_SEED))
        );

        CkcPayload ckcPayload = new CkcPayload(
                ckcSecurityContext.encryptCk(
                        ck,
                        spcSkR1
                ),
                new ContentKeyIv(ck.getIv()),
                new CkcR1(spcSkR1.getR1()),
                new CkcContentKeyDuration(),
                tllvContainer.findReturnRequestBlocks()
        );

        CkcDataIv ckcDataIv = CkcDataIv.generate();
        CkcEncryptedPayload ckcEncryptedPayload = ckcSecurityContext.encryptCkcPayload(ckcPayload, ckcDataIv);

        return new Ckc(ckcDataIv, ckcEncryptedPayload);
    }
}
