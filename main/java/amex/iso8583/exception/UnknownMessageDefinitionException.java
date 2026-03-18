package amex.iso8583.exception;

public class UnknownMessageDefinitionException extends DecodeException {

    public UnknownMessageDefinitionException(String message, String mti, Throwable cause) {
        super(message, mti, null, null, null, cause);
    }
}