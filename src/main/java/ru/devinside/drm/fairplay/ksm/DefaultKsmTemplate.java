package ru.devinside.drm.fairplay.ksm;

import ru.devinside.drm.fairplay.ksm.ckc.*;
import ru.devinside.drm.fairplay.ksm.common.TllvBlock;
import ru.devinside.drm.fairplay.ksm.secret.ContentKey;
import ru.devinside.drm.fairplay.ksm.secret.FpsCertificate;
import ru.devinside.drm.fairplay.ksm.spc.*;
import ru.devinside.drm.fairplay.ksm.spc.tags.SkR1Integrity;
import ru.devinside.drm.fairplay.ksm.spc.tags.SpcR2;
import ru.devinside.drm.fairplay.ksm.spc.tags.SpcSkR1;
import ru.devinside.util.Hexler;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
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
    public Ckc compute(
            Spc spc,
            Function<? super ClientPlaybackContext, ? extends ContentKey> contentKeyCallback,
            Consumer<? super ClientServerProtocolCompatibility> compatibilityCallback
    ) {
        security.ensureCorrectFpsCertificateUsed(spc);

        TllvBlockIndex tllvBlockIndex = TllvBlockIndex.build(
                new SpcPayloadReader(decryptPayload(spc))
        );

        checkClientServerCompatibility(tllvBlockIndex, compatibilityCallback);

        SpcSkR1 spcSkR1 = decryptSkR1(tllvBlockIndex);

        ClientPlaybackContext context = new ClientPlaybackContext(
                findAssetId(tllvBlockIndex),
                findStreamingIndicator(tllvBlockIndex).orElse(null),
                findClientCapabilities(tllvBlockIndex).orElse(null),
                Hexler.toHexString(spcSkR1.getHu())
        );

        return generateCkc(tllvBlockIndex, spcSkR1, contentKeyCallback.apply(context));
    }

    /**
     * TODO: StreamingIndicator should always be in SPC but SDK SPC tests don't include it.
     */
    private Optional<StreamingIndicator> findStreamingIndicator(TllvBlockIndex tllvBlockIndex) {
        TllvBlock streamingIndicator = tllvBlockIndex.find(SpcTag.STREAMING_INDICATOR);
        if(streamingIndicator != null) {
            try {
                ByteBuffer buffer = ByteBuffer.wrap(streamingIndicator.getValue());
                return Optional.of(StreamingIndicator.from(buffer.getLong()));
            } catch (BufferUnderflowException e) {
                throw new BadSpcException(String.format("Unable to parse %s TLLV", SpcTag.STREAMING_INDICATOR));
            }
        }
        return Optional.empty();
    }

    private Optional<ClientCapabilities> findClientCapabilities(TllvBlockIndex tllvBlockIndex) {
        TllvBlock clientCapabilities = tllvBlockIndex.find(SpcTag.CAPABILITIES);
        if(clientCapabilities != null) {
            try {
                ByteBuffer buffer = ByteBuffer.wrap(clientCapabilities.getValue());
                return Optional.of(
                        new ClientCapabilities(buffer.getLong() == 1, buffer.getLong() == 1)
                );
            } catch (BufferUnderflowException e) {
                throw new BadSpcException(String.format("Unable to parse %s TLLV", SpcTag.CAPABILITIES));
            }
        }
        return Optional.empty();
    }

    private AssetId findAssetId(TllvBlockIndex tllvBlockIndex) {
        TllvBlock assetIdTllvBlock = tllvBlockIndex.find(SpcTag.ASSET_ID);
        if(assetIdTllvBlock == null) {
            throw new BadSpcException(String.format("The %s TLLV block not found", SpcTag.ASSET_ID));
        }
        return new AssetId(assetIdTllvBlock.getValue());
    }

    private SpcPayload decryptPayload(Spc spc) {
        SpcKey spcKey = security.decryptSpcKey(spc.getEncryptedSpcKey());
        return security.decryptPayload(spc, spcKey);
    }

    private SpcSkR1 decryptSkR1(TllvBlockIndex tllvBlockIndex) {
        TllvBlock tllvSkR1 = tllvBlockIndex.find(SpcTag.SK_R1);
        SpcEncryptedSkR1 spcEncryptedSkR1 = new SpcEncryptedSkR1(tllvSkR1);

        TllvBlock tllvR2 = tllvBlockIndex.find(SpcTag.R2);
        SpcR2 spcR2 = new SpcR2(tllvR2);

        SpcSkR1 spcSkR1 = security.decryptSkR1(spcEncryptedSkR1, spcR2);

        TllvBlock tllvSkR1Integrity = tllvBlockIndex.find(SpcTag.SK_R1_INTEGRITY);

        if(!security.integrityCheckSkR1(spcSkR1, new SkR1Integrity(tllvSkR1Integrity))) {
            throw new BadSpcException("SPC [SK..R1] integrity violation");
        }

        return spcSkR1;
    }

    private void checkClientServerCompatibility(
            TllvBlockIndex tllvBlockIndex,
            Consumer<? super ClientServerProtocolCompatibility> compatibilityCallback
    ) {
        Collection<Integer> supported = ProtocolVersionsSupportedParser.INSTANCE.parse(
                tllvBlockIndex.find(SpcTag.PROTOCOL_VERSIONS_SUPPORTED)
        );

        Integer used = ProtocolVersionUsedParser.INSTANCE.parse(
                tllvBlockIndex.find(SpcTag.PROTOCOL_VERSION_USED)
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
    }

    @Override
    public FpsCertificate fpsCertificate() {
        return security.getCertificate();
    }

    @Override
    public Collection<Integer> protocolVersions() {
        return SERVER_KSM_PROTOCOL_VERSIONS;
    }

    private Ckc generateCkc(TllvBlockIndex tllvBlockIndex, SpcSkR1 spcSkR1, ContentKey ck) {
        CkcSecurityService ckcSecurityService = new CkcSecurityService(
                spcSkR1,
                new SpcArSeed(tllvBlockIndex.find(SpcTag.AR_SEED))
        );

        CkcPayload ckcPayload = new CkcPayload(
                ckcSecurityService.encryptCk(
                        ck,
                        spcSkR1
                ),
                new ContentKeyIv(ck.getIv()),
                new CkcR1(spcSkR1.getR1()),
                new CkcContentKeyDuration(),
                tllvBlockIndex.findReturnRequestBlocks()
        );

        CkcDataIv ckcDataIv = CkcDataIv.generate();
        CkcEncryptedPayload ckcEncryptedPayload = ckcSecurityService.encryptCkcPayload(ckcPayload, ckcDataIv);

        return new Ckc(ckcDataIv, ckcEncryptedPayload);
    }
}
