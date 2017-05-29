package ru.devinside.drm.fairplay.ksm.spc;

import ru.devinside.drm.fairplay.ksm.common.TllvBlock;

import java.nio.ByteBuffer;

public class SpcPayloadParser {
    public SpcTllvContainer parse(SpcPayload scpPayload) {
        ByteBuffer payload = ByteBuffer.wrap(scpPayload.getPayload());
        SpcTllvContainer tllvContainer = new SpcTllvContainer();
        while(payload.hasRemaining()) {
            long tag = payload.getLong();
            int totalBlockLength = payload.getInt();
            int valueLength = payload.getInt();

            // TODO: throw illegal data exception for each assert
            // assert: totalBlockLength > 0
            // assert: valueLength < totalBlockLength
            // assert: valueLength >= 0

            byte[] value = new byte[valueLength];

            if (valueLength > 0) {
                payload.get(value);
            }

            int paddingLength = totalBlockLength - valueLength;
            byte[] padding = new byte[paddingLength];
            if(paddingLength > 0 ) {
                payload.get(padding);
            }

            tllvContainer.add(new TllvBlock(tag, value, totalBlockLength, padding));

            // TODO: remove debug prints
            System.out.println("TLLV tag: " + Long.toHexString(tag) + " " + SpcTag.valueOf(tag));
            System.out.println("TLLV block length: " + totalBlockLength);
            System.out.println("TLLV value length: " + valueLength);
            System.out.println("TLLV value: " + value);
            System.out.println("skipe padding: " + paddingLength + " bytes");
        }

        return tllvContainer;
    }
}
