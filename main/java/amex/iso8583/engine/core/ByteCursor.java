package amex.iso8583.engine.core;

import amex.iso8583.exception.UnexpectedEndOfMessageException;

import java.util.Arrays;

public class ByteCursor {

    private final byte[] source;
    private int position;

    public ByteCursor(byte[] source) {
        if (source == null) {
            throw new IllegalArgumentException("source must not be null");
        }
        this.source = source;
        this.position = 0;
    }

    public int position() {
        return position;
    }

    public int remaining() {
        return source.length - position;
    }

    public boolean hasRemaining() {
        return remaining() > 0;
    }

    public byte readByte() {
        ensureAvailable(1);
        return source[position++];
    }

    public byte peek() {
        ensureAvailable(1);
        return source[position];
    }

    public byte peek(int offset) {
        if (offset < 0) {
            throw new IllegalArgumentException("offset must be >= 0");
        }
        ensureAvailable(offset + 1);
        return source[position + offset];
    }

    public byte[] readBytes(int length) {
        if (length < 0) {
            throw new IllegalArgumentException("length must be >= 0");
        }
        ensureAvailable(length);

        byte[] result = Arrays.copyOfRange(source, position, position + length);
        position += length;
        return result;
    }

    public byte[] peekBytes(int length) {
        if (length < 0) {
            throw new IllegalArgumentException("length must be >= 0");
        }
        ensureAvailable(length);
        return Arrays.copyOfRange(source, position, position + length);
    }

    public void skip(int length) {
        if (length < 0) {
            throw new IllegalArgumentException("length must be >= 0");
        }
        ensureAvailable(length);
        position += length;
    }

    public byte[] slice(int startInclusive, int endExclusive) {
        if (startInclusive < 0 || endExclusive < startInclusive || endExclusive > source.length) {
            throw new IllegalArgumentException("invalid slice range");
        }
        return Arrays.copyOfRange(source, startInclusive, endExclusive);
    }

    public byte[] source() {
        return source;
    }

    private void ensureAvailable(int required) {
        if (remaining() < required) {
            throw new UnexpectedEndOfMessageException(
                    "Not enough bytes in message",
                    null,
                    null,
                    position,
                    required,
                    remaining(),
                    null
            );
        }
    }
}