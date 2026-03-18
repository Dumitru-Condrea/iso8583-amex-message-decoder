package amex.iso8583.exception;

import lombok.Getter;

@Getter
public class InvalidLengthException extends DecodeException {

    private final Integer actualLength;
    private final Integer expectedLength;

    public InvalidLengthException(
            String message,
            String mti,
            Integer fieldNumber,
            String fieldName,
            Integer offset,
            Throwable cause
    ) {
        super(message, mti, fieldNumber, fieldName, offset, cause);
        this.actualLength = null;
        this.expectedLength = null;
    }

    public InvalidLengthException(
            String message,
            String mti,
            Integer fieldNumber,
            String fieldName,
            Integer offset,
            Integer actualLength,
            Integer expectedLength,
            Throwable cause
    ) {
        super(message, mti, fieldNumber, fieldName, offset, cause);
        this.actualLength = actualLength;
        this.expectedLength = expectedLength;
    }
}