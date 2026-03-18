package amex.iso8583.engine.core;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DecodedSubfield {

    private final int subfieldNumber;
    private final String subfieldName;
    private final String displayValue;
    private final DecodedFieldMeta meta;

}
