package amex.iso8583.engine.value;

import java.io.UnsupportedEncodingException;

public class ValueDecoder {

    private static final String EBCDIC_CP500 = "Cp500";
    private static final char[] HEX = "0123456789ABCDEF".toCharArray();

    public String decodeEbcdic(byte[] value) {
        try {
            return new String(value, EBCDIC_CP500);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("EBCDIC charset not supported: " + EBCDIC_CP500, e);
        }
    }

    public String decodeAscii(byte[] value) {
        return new String(value);
    }

    public String decodeBcd(byte[] value, int logicalLengthDigits) {
        StringBuilder sb = new StringBuilder(value.length * 2);

        for (int i = 0; i < value.length; i++) {
            int unsigned = value[i] & 0xFF;
            int hi = (unsigned >>> 4) & 0x0F;
            int lo = unsigned & 0x0F;
            boolean last = i == value.length - 1;

            if (last && logicalLengthDigits % 2 != 0) {
                if (lo == 0x0F) {
                    appendDigit(sb, hi);
                } else if (hi == 0x0F) {
                    appendDigit(sb, lo);
                } else {
                    appendDigit(sb, hi);
                }
            } else {
                appendDigit(sb, hi);
                appendDigit(sb, lo);
            }
        }

        if (logicalLengthDigits > 0 && sb.length() > logicalLengthDigits) {
            return sb.substring(0, logicalLengthDigits);
        }
        return sb.toString();
    }

    public String decodeBinaryAsHex(byte[] value) {
        return toHex(value);
    }

    public String decodeHexBytesAsHex(byte[] value) {
        return toHex(value);
    }

    public String toHex(byte[] value) {
        char[] out = new char[value.length * 2];
        int pos = 0;

        for (byte b : value) {
            int v = b & 0xFF;
            out[pos++] = HEX[v >>> 4];
            out[pos++] = HEX[v & 0x0F];
        }

        return new String(out);
    }

    private void appendDigit(StringBuilder sb, int nibble) {
        if (nibble < 0 || nibble > 9) {
            throw new IllegalArgumentException("Invalid BCD nibble: " + nibble);
        }
        sb.append((char) ('0' + nibble));
    }

    public String decodeMaskedPan(byte[] value) {
        StringBuilder sb = new StringBuilder(value.length);

        for (byte b : value) {
            int unsigned = b & 0xFF;

            if (unsigned >= 0xF0 && unsigned <= 0xF9) {
                sb.append((char) ('0' + (unsigned - 0xF0)));
            } else if (unsigned == 0x2A) {
                sb.append('*');
            } else if (unsigned == 0x40) {
                sb.append('~');
            } else {
                String decoded = decodeEbcdic(new byte[]{b});
                sb.append(decoded != null && decoded.length() == 1 ? decoded.charAt(0) : '?');
            }
        }

        return sb.toString();
    }

    public String normalizeDisplaySpaces(String value) {
        if (value == null) {
            return null;
        }
        return value.replace(' ', '~');
    }

    public static byte[] hexToBytes(String hex) {
        if (hex == null) {
            throw new IllegalArgumentException("hex must not be null");
        }

        String normalized = hex.replaceAll("\\s+", "");
        if ((normalized.length() % 2) != 0) {
            throw new IllegalArgumentException("hex length must be even");
        }

        byte[] out = new byte[normalized.length() / 2];

        for (int i = 0; i < normalized.length(); i += 2) {
            int hi = Character.digit(normalized.charAt(i), 16);
            int lo = Character.digit(normalized.charAt(i + 1), 16);

            if (hi < 0 || lo < 0) {
                throw new IllegalArgumentException("invalid hex character at position " + i);
            }

            out[i / 2] = (byte) ((hi << 4) | lo);
        }

        return out;
    }
}