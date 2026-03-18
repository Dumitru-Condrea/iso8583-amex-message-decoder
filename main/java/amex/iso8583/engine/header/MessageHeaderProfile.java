package amex.iso8583.engine.header;

import amex.iso8583.schema.common.DataEncoding;
import lombok.Getter;

@Getter
public class MessageHeaderProfile {

    private final DataEncoding mtiEncoding;
    private final DataEncoding bitmapEncoding;
    private final boolean secondaryBitmapSupported;

    public MessageHeaderProfile(DataEncoding mtiEncoding, DataEncoding bitmapEncoding, boolean secondaryBitmapSupported) {
        if (mtiEncoding == null) {
            throw new IllegalArgumentException("mtiEncoding must not be null");
        }

        if (bitmapEncoding == null) {
            throw new IllegalArgumentException("bitmapEncoding must not be null");
        }

        this.mtiEncoding = mtiEncoding;
        this.bitmapEncoding = bitmapEncoding;
        this.secondaryBitmapSupported = secondaryBitmapSupported;
    }

    public static MessageHeaderProfile amexDefault() {
        return new MessageHeaderProfile(DataEncoding.EBCDIC, DataEncoding.BINARY, true);
    }
}
