package amex.iso8583.model.field;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AmexSafeKeyData {

    private final String primaryId;
    private final String secondaryId;
    private final String eci;
    private final String aevvId;
    private final String aevv;
    private final String xid;
    private final String amexSafeKeyTransactionIdValue;
}
