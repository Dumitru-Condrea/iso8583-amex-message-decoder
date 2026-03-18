package amex.iso8583.exception;

public class UnknownFieldDefinitionException extends DecodeException {

    public UnknownFieldDefinitionException(String message, String mti, Integer fieldNumber, Integer offset, Throwable cause) {
        super(message, mti, fieldNumber, null, offset, cause);
    }
}
