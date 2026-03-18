package amex.iso8583.exception;

import amex.iso8583.engine.core.DecodedSubfield;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class CompositeDecodeException extends DecodeException {

    private final List<DecodedSubfield> decodedSubfields;

    public CompositeDecodeException(
            String message,
            String mti,
            Integer fieldNumber,
            String fieldName,
            Integer offset,
            List<DecodedSubfield> decodedSubfields,
            Throwable cause
    ) {
        super(message, mti, fieldNumber, fieldName, offset, cause);

        this.decodedSubfields = Collections.unmodifiableList(
                new ArrayList<>(decodedSubfields == null
                        ? Collections.emptyList()
                        : decodedSubfields
                )
        );
    }
}
