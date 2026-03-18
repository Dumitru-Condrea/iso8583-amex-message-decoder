package amex.iso8583.exception;

import lombok.Getter;

@Getter
public class DecodeException extends RuntimeException {

    private final String mti;
    private final Integer fieldNumber;
    private final String fieldName;
    private final Integer offset;

    public DecodeException(String message) {
        super(message);
        this.mti = null;
        this.fieldNumber = null;
        this.fieldName = null;
        this.offset = null;
    }

    public DecodeException(String message, Throwable cause) {
        super(message, cause);
        this.mti = null;
        this.fieldNumber = null;
        this.fieldName = null;
        this.offset = null;
    }

    public DecodeException(
            String message,
            String mti,
            Integer fieldNumber,
            String fieldName,
            Integer offset,
            Throwable cause
    ) {
        super(message, cause);
        this.mti = mti;
        this.fieldNumber = fieldNumber;
        this.fieldName = fieldName;
        this.offset = offset;
    }
}
