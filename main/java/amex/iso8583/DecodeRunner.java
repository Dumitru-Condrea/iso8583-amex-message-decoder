package amex.iso8583;

import amex.iso8583.api.AmexDecoder;
import amex.iso8583.api.DecodedAmexResult;
import amex.iso8583.model.common.AmexMessage;

import java.util.Map;

public class DecodeRunner {

    public static void main(String[] args) {

        String rawHex = "";

        byte[] rawMessage = hexToBytes(rawHex);

        AmexDecoder decoder = new AmexDecoder();

        try {
            DecodedAmexResult<? extends AmexMessage> result = decoder.decode(rawMessage);

            System.out.println("MTI: " + result.getMessage().getMti());
            System.out.println("---- FLAT VALUES ----");

            for (Map.Entry<String, String> entry : result.getFlatValues().entrySet()) {
                System.out.println(entry.getKey() + " = " + entry.getValue());
            }

            System.out.println("---- FIELD META ----");
            for (Map.Entry<Integer, amex.iso8583.engine.core.DecodedFieldMeta> entry : result.getFieldMeta().entrySet()) {
                amex.iso8583.engine.core.DecodedFieldMeta meta = entry.getValue();
                System.out.println(
                        "Field " + entry.getKey()
                                + " [" + meta.getFieldName() + "]"
                                + " start=" + meta.getStartOffset()
                                + " end=" + meta.getEndOffset()
                                + " rawLength=" + meta.getRawLength()
                                + " rawHex=" + meta.getRawHex()
                );
            }

        } catch (Exception e) {
            System.err.println("Decode failed:");
            e.printStackTrace();
        }
    }

    private static byte[] hexToBytes(String hex) {
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
