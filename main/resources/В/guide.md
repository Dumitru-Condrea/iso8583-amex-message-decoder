h1. Quick Start Guide

h2. 1. Basic Decode

{code:java}
AmexDecoder decoder = new AmexDecoder();

DecodedAmexResult<? extends AmexMessage> result =
decoder.decode(rawMessage);
{code}

---

h2. 2. Access Typed Message

If the message type is known:

{code:java}
DecodedAmexResult<Amex1100Message> result =
decoder.decode(rawMessage, new Amex1100Definition(), DecodeOptions.empty());

Amex1100Message message = result.getMessage();

System.out.println(message.getPan());
System.out.println(message.getStan());
{code}

---

h2. 3. Use Decode Options

Optional flags can change decoding behavior:

{code:java}
DecodeOptions options = DecodeOptions.empty()
.with(DecodeOptionKeys.DE55_VARIANT, "SIMPLIFIED");

DecodedAmexResult<? extends AmexMessage> result =
decoder.decode(rawMessage, options);
{code}

---

h2. 4. Debug Output

Use Pretty Printer for debugging:

{code:java}
DecodedPrettyPrinter.print(
result,
rawMessage,
System.out,
Arrays.asList("PAN", "P55")
);
{code}

---

h3. Highlight Fields

Supports:
* PID → P2, P55.1
* Field name → PAN, POS_DATA_CODE.CARD_PRESENT

---

h2. 5. Disable Colors

For logs or CI:

{code:java}
DecodedPrettyPrinter.setAnsiMode(DecodedPrettyPrinter.AnsiMode.OFF);
{code}

---

h2. 6. Typical Workflow

1. Load raw message (byte[])
2. Decode using AmexDecoder
3. Use typed message for business logic
4. Use PrettyPrinter for debugging

---

h2. 7. Example Runner

{code:java}
public class DecodeRunner {

    public static void main(String[] args) {

        byte[] rawMessage = ...;

        AmexDecoder decoder = new AmexDecoder();

        DecodeOptions options = DecodeOptions.empty()
                .with(DecodeOptionKeys.DE55_VARIANT, "SIMPLIFIED");

        DecodedAmexResult<? extends AmexMessage> result =
                decoder.decode(rawMessage, options);

        DecodedPrettyPrinter.print(
                result,
                rawMessage,
                System.out,
                Arrays.asList("PAN", "P22.6")
        );
    }
}
{code}

---

h2. 8. Tips

* Use typed messages for business logic
* Use flatValues for debugging
* Keep DecodeOptions optional
* Prefer schema adjustments over custom parsing logic