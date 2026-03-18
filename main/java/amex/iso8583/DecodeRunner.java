package amex.iso8583;

import amex.iso8583.api.AmexDecoder;
import amex.iso8583.api.DecodedAmexResult;
import amex.iso8583.model.common.AmexMessage;
import amex.iso8583.util.DecodedPrettyPrinter;

import static amex.iso8583.engine.value.ValueDecoder.hexToBytes;

public class DecodeRunner {

    public static void main(String[] args) {
        AmexDecoder decoder = new AmexDecoder();
        DecodedPrettyPrinter.setAnsiMode(DecodedPrettyPrinter.AnsiMode.ON);

        String rawHex = "";
        byte[] rawMessage = hexToBytes(rawHex);

        DecodedAmexResult<? extends AmexMessage> result = decoder.decode(rawMessage);
        DecodedPrettyPrinter.print(
                result,
                hexToBytes(rawHex),
                System.out,
                java.util.Arrays.asList("MTI", "PRIMARY_BITMAP", "SECONDARY_BITMAP", "PAN", "POS_DATA_CODE", "p112.1")
        );
    }
}
