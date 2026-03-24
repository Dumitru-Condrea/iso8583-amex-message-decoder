package amex.iso8583.model.field;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PrimaryAccountNumber {

    private final String raw;
    private final String start;
    private final String end;
}