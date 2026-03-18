package amex.iso8583.engine.core;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class DecodedField {

    private final int fieldNumber;
    private final String fieldName;
    private final String displayValue;
    private final List<DecodedSubfield> subfields;
    private final DecodedFieldMeta meta;

    public DecodedField(
            int fieldNumber,
            String fieldName,
            String displayValue,
            List<DecodedSubfield> subfields,
            DecodedFieldMeta meta
    ) {
        this.fieldNumber = fieldNumber;
        this.fieldName = fieldName;
        this.displayValue = displayValue;

        this.subfields = Collections.unmodifiableList(
                new ArrayList<>(subfields == null
                        ? Collections.<DecodedSubfield>emptyList()
                        : subfields)
        );

        this.meta = meta;
    }

    public boolean hasSubfields() {
        return !subfields.isEmpty();
    }
}
