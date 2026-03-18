package amex.iso8583.engine.core;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DecodedFieldMeta {

    private final int fieldNumber;
    private final String fieldName;
    private final int startOffset;
    private final int endOffset;
    private final int rawLength;
    private final String rawHex;

}
