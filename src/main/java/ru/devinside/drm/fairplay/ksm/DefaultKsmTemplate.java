package ru.devinside.drm.fairplay.ksm;

import ru.devinside.drm.fairplay.ksm.ckc.*;
import ru.devinside.drm.fairplay.ksm.common.BinVal;
import ru.devinside.drm.fairplay.ksm.common.TllvBlock;
import ru.devinside.drm.fairplay.ksm.secret.ContentKey;
import ru.devinside.drm.fairplay.ksm.spc.*;
import ru.devinside.drm.fairplay.ksm.spc.tags.SkR1Integrity;
import ru.devinside.drm.fairplay.ksm.spc.tags.SpcR2;
import ru.devinside.drm.fairplay.ksm.spc.tags.SpcSkR1;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Consumer;
import java.util.function.Function;

public class DefaultKsmTemplate implements KsmTemplate {
    private static final int KSM_PROTOCOL_V1 = 1;

    private final static Collection<Integer> SERVER_KSM_PROTOCOL_VERSIONS = Collections.singleton(KSM_PROTOCOL_V1);

    private final SpcSecurityService security;

    public DefaultKsmTemplate(SpcSecurityService security) {
        this.security = security;
    }

    @Override
    public Ckc process(
            Spc spc,
            Function<? super AssetId, ? extends ContentKey> contentKeyCallback,
            Consumer<? super ClientServerProtocolCompatibility> compatibilityCallback
    ) {
        SpcKey spcKey = security.decryptSpcKey(spc.getEncryptedSpcKey());
        SpcPayload payload = security.decryptPayload(spc, spcKey);

        SpcTllvIndex tllvContainer = new SpcPayloadReader(payload).index();

        Collection<Integer> supported = ProtocolVersionsSupportedParser.INSTANCE.parse(
                tllvContainer.find(SpcTag.PROTOCOL_VERSIONS_SUPPORTED)
        );

        Integer used = ProtocolVersionUsedParser.INSTANCE.parse(
                tllvContainer.find(SpcTag.PROTOCOL_VERSION_USED)
        );

        ClientServerProtocolCompatibility compatibility = ClientServerProtocolCompatibilityChecker.INSTANCE.check(
                protocolVersions(),
                used,
                supported
        );
        compatibilityCallback.accept(compatibility);

        if(!compatibility.isCompatible()) {
            throw new SpcClientServerCompatibilityException(
                    String.format("Server is not compatible with the client version %s", used)
            );
        }

        TllvBlock tllvSkR1 = tllvContainer.find(SpcTag.SK_R1);
        SpcSkR1Raw spcSkR1Raw = new SpcSkR1Raw(tllvSkR1);

        TllvBlock tllvR2 = tllvContainer.find(SpcTag.R2);
        SpcR2 spcR2 = new SpcR2(tllvR2);

        SpcSkR1 spcSkR1 = security.decryptSkR1(spcSkR1Raw, spcR2);

        TllvBlock tllvSkR1Integrity = tllvContainer.find(SpcTag.SK_R1_INTEGRITY);

        if(!security.integrityCheckSkR1(spcSkR1, new SkR1Integrity(tllvSkR1Integrity))) {
            throw new BadSpcException("SPC [SK..R1] integrity violation");
        }

        AssetId assetId = new AssetId(new BinVal(tllvContainer.find(SpcTag.ASSET_ID).getValue()));

        return generateCkc(spcSkR1, tllvContainer, contentKeyCallback.apply(assetId));
    }

    @Override
    public Collection<Integer> protocolVersions() {
        return SERVER_KSM_PROTOCOL_VERSIONS;
    }

    private Ckc generateCkc(SpcSkR1 spcSkR1, SpcTllvIndex tllvContainer, ContentKey ck) {
        CkcHelper ckcHelper = new CkcHelper(
                spcSkR1,
                new SpcArSeed(tllvContainer.find(SpcTag.AR_SEED))
        );

        CkcPayload ckcPayload = new CkcPayload(
                ckcHelper.encryptCk(
                        ck,
                        spcSkR1
                ),
                new ContentKeyIv(ck.getIv()),
                new CkcR1(spcSkR1.getR1()),
                new CkcContentKeyDuration(),
                tllvContainer.findReturnRequestBlocks()
        );

        CkcDataIv ckcDataIv = CkcDataIv.generate();
        CkcEncryptedPayload ckcEncryptedPayload = ckcHelper.encryptCkcPayload(ckcPayload, ckcDataIv);

        return new Ckc(ckcDataIv, ckcEncryptedPayload);
    }
}
