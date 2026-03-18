package amex.iso8583.exception;

import lombok.Getter;

@Getter
public class UnexpectedEndOfMessageException extends DecodeException {

    private final Integer expectedBytes;
    private final Integer remainingBytes;

    public UnexpectedEndOfMessageException(
            String message,
            String mti,
            Integer fieldNumber,
            Integer offset,
            Integer expectedBytes,
            Integer remainingBytes,
            Throwable cause
    ) {
        super(message, mti, fieldNumber, null, offset, cause);
        this.expectedBytes = expectedBytes;
        this.remainingBytes = remainingBytes;
    }
}
