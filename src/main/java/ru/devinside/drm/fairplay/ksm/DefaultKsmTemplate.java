package ru.devinside.drm.fairplay.ksm;

import ru.devinside.drm.fairplay.ksm.ckc.*;
import ru.devinside.drm.fairplay.ksm.common.TllvBlock;
import ru.devinside.drm.fairplay.ksm.secret.ApplicationSecretKey;
import ru.devinside.drm.fairplay.ksm.secret.ContentKey;
import ru.devinside.drm.fairplay.ksm.spc.*;
import ru.devinside.drm.fairplay.ksm.spc.tags.SkR1Integrity;
import ru.devinside.drm.fairplay.ksm.spc.tags.SpcR2;
import ru.devinside.drm.fairplay.ksm.spc.tags.SpcSkR1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class DefaultKsmTemplate implements KsmTemplate {
    private final SpcMessageParser spcMessageParser = new SpcMessageParser();
    private final SpcPayloadParser payloadParser = new SpcPayloadParser();
    private final SpcSecurityContext securityContext;

    public DefaultKsmTemplate(SpcSecurityContext securityContext) {
        this.securityContext = securityContext;
    }

    @Override
    public byte[] generateCkc(byte[] spc) throws IOException {
        SpcMessage spcMessage = spcMessageParser.parse(spc);

        Spck spck = securityContext.decryptSpck(spcMessage.getEncryptedSpck());
        SpcPayload payload = securityContext.decryptPayload(spcMessage.getPayload(), spck, spcMessage.getIv());

        SpcTllvContainer tllvContainer = payloadParser.parse(payload);

        TllvBlock tllvSkR1 = tllvContainer.find(SpcTag.SK_R1);
        SpcSkR1Raw spcSkR1Raw = new SpcSkR1Raw(tllvSkR1);

        TllvBlock tllvR2 = tllvContainer.find(SpcTag.R2);
        SpcR2 spcR2 = new SpcR2(tllvR2);

        SpcSkR1 spcSkR1 = securityContext.decryptSkR1(spcSkR1Raw, spcR2, new ApplicationSecretKey());

        TllvBlock tllvSkR1Integrity = tllvContainer.find(SpcTag.SK_R1_INTEGRITY);

        if(!securityContext.integrityCheckSkR1(spcSkR1, new SkR1Integrity(tllvSkR1Integrity))) {
            throw new IllegalArgumentException("Bad SPC message");
        }

        //
        // CKC generation part begins here:
        //

        CkcDataIv ckcDataIv = CkcDataIv.generate();

        CkcSecurityContext ckcSecurityContext = new CkcSecurityContext(
                spcSkR1,
                new SpcArSeed(tllvContainer.find(SpcTag.AR_SEED))
        );

        CkcPayload ckcPayload = new CkcPayload(
                ckcDataIv,
                ckcSecurityContext.encryptCk(
                        new ContentKey(), // TODO: CK must be located before
                        spcSkR1
                ),
                new CkcR1(spcSkR1.getR1()),
                new CkcContentKeyDuration(),
                tllvContainer.findReturnRequestBlocks()
        );

        CkcEncryptedPayload ckcEncryptedPayload = ckcSecurityContext.encryptCkcPayload(ckcPayload, ckcDataIv);

        CkcMessage ckcMessage = new CkcMessage(ckcDataIv, ckcEncryptedPayload);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        new CkcMessageSerializer(ckcMessage).serializeTo(out);

        return out.toByteArray();
    }
}
