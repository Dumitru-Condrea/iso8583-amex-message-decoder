package amex.iso8583.model.field;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NationalUseData {

    private final String primaryId;
    private final String secondaryId;
    private final String bitmapIdentifier;
    private final String sellerId;
    private final String otid;
}
