package amex.iso8583.engine.header;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class BitmapSet {

    private final String primaryHex;
    private final String secondaryHex;
    private final List<Integer> activeFields;

    public BitmapSet(String primaryHex, String secondaryHex, List<Integer> activeFields) {
        if (primaryHex == null) {
            throw new IllegalArgumentException("primaryHex must not be null");
        }

        if (activeFields == null) {
            throw new IllegalArgumentException("activeFields must not be null");
        }

        this.primaryHex = primaryHex;
        this.secondaryHex = secondaryHex;
        this.activeFields = Collections.unmodifiableList(new ArrayList<Integer>(activeFields));
    }

    public boolean hasSecondaryBitmap() {
        return secondaryHex != null;
    }

    public boolean containsField(int fieldNumber) {
        return activeFields.contains(fieldNumber);
    }
}