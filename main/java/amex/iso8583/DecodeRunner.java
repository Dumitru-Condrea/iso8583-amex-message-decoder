package amex.iso8583;

import amex.iso8583.api.AmexDecoder;
import amex.iso8583.api.DecodeOptionKeys;
import amex.iso8583.api.DecodeOptions;
import amex.iso8583.api.DecodedAmexResult;
import amex.iso8583.model.common.AmexMessage;
import amex.iso8583.util.DecodedPrettyPrinter;

import static amex.iso8583.engine.value.ValueDecoder.hexToBytes;

public class DecodeRunner {

    public static void main(String[] args) {
        AmexDecoder decoder = new AmexDecoder();
        DecodedPrettyPrinter.setAnsiMode(DecodedPrettyPrinter.AnsiMode.ON);

        String rawHex = "F1F1F0F0F03425C000E088000000000000010000F1F5F3F7F9F5F1F02A2A2A2A2AF0F0F1F9F0F0F4F0F0F0F0F0F0F0F0F0F0F0F2F9F9F8F7F8F4F0F1F6F2F6F0F3F1F8F1F4F4F5F3F92A2A2A2AF8F2F6F1F0F0F9E2F0E2F0F0F0F0F1F1F0F0F1F9F0F0F7F5F2F3F1F0F1F3F0F5F7F5F1F2F3F4F5F6F7F340404040404040F3F9C1D4C5E7F240C183838597A3969940899586967EE0E0E040404040404040404040404040F8F2F6F7F5F2F0F4F1F3F3F4F0F4F4C1E7C1C5F180000000F9F9F9F9F9F9F9F9F9F9F9F9F9F9F9F9F9F9F9F9F9F9F9F9F9F9F9F9F9F9F9F9F9F9F9";
        byte[] rawMessage = hexToBytes(rawHex);

        DecodedAmexResult<? extends AmexMessage> result = decoder.decode(rawMessage);

        //bool
//        DecodeOptions options = DecodeOptions.empty()
//                .with(DecodeOptionKeys.DE55_SIMPLIFIED_EMV, Boolean.TRUE);
//
//        DecodedAmexResult<? extends AmexMessage> result = decoder.decode(rawMessage, options);


       // var
//        DecodeOptions options = DecodeOptions.empty()
//                .with(DecodeOptionKeys.DE55_VARIANT, "LEGACY");
//
//        DecodedAmexResult<? extends AmexMessage> result = decoder.decode(rawMessage, options);

        //combined
//        DecodeOptions options = DecodeOptions.empty()
//                .with(DecodeOptionKeys.DE55_SIMPLIFIED_EMV, Boolean.TRUE)
//                .with(DecodeOptionKeys.DE55_VARIANT, "WITH_EXTRA_TAG");
//
//        DecodedAmexResult<? extends AmexMessage> result = decoder.decode(rawMessage, options);

        DecodedPrettyPrinter.print(
                result,
                hexToBytes(rawHex),
                System.out,
                java.util.Arrays.asList("MTI", "PRIMARY_BITMAP", "SECONDARY_BITMAP", "PAN", "POS_DATA_CODE", "p112.1")
        );
    }
}
