package ru.devinside.drm.fairplay.ksm.spc;

import ru.devinside.drm.fairplay.ksm.common.TllvBlock;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * SCP TLLV blocks reader
 */
public class SpcPayloadReader implements Iterator<TllvBlock> {
    private final ByteBuffer payload;

    public SpcPayloadReader(SpcPayload scpPayload) {
        payload = ByteBuffer.wrap(scpPayload.getPayload());
    }

    @Override
    public boolean hasNext() {
        return payload.hasRemaining();
    }

    @Override
    public TllvBlock next() {
        if(!hasNext()) {
            throw new NoSuchElementException();
        }

        long tag = parseTag(payload);
        int totalBlockLength = parseTotalBlockLength(payload);
        int valueLength = parseValueLength(payload);

        if(valueLength >= totalBlockLength) {
            throw new BadSpcException(String.format("Inconsistent TLLV block header: %s", tag));
        }

        byte[] value = new byte[valueLength];

        if (valueLength > 0) {
            payload.get(value);
        }

        int paddingLength = totalBlockLength - valueLength;
        byte[] padding = new byte[paddingLength];
        if(paddingLength > 0 ) {
            payload.get(padding);
        }

        return new TllvBlock(tag, value, totalBlockLength, padding);
    }

    private static long parseTag(ByteBuffer payload) {
        try {
            return payload.getLong();
        } catch (BufferUnderflowException e) {
            throw new BadSpcException("Unable to parse tag", e);
        }
    }

    private int parseTotalBlockLength(ByteBuffer payload) {
        try {
            int totalBlockLength = payload.getInt();

            if(totalBlockLength < 1) {
                throw new BadSpcException("Total block length must be a positive number");
            }

            return totalBlockLength;
        } catch (BufferUnderflowException e) {
            throw new BadSpcException("Unable to parse TLLV total block length", e);
        }
    }

    private int parseValueLength(ByteBuffer payload) {
        try {
            int valueLength = payload.getInt();

            if(valueLength < 0) {
                throw new BadSpcException("Total value length must not be a negative number");
            }

            return valueLength;
        } catch (BufferUnderflowException e) {
            throw new BadSpcException("Unable to parse TLLV value length", e);
        }
    }
}
