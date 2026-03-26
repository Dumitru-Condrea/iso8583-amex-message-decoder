h2. 0. Setup & Run

h3. Clone Repository

Clone the project from your repository:

{code:bash}
git clone <repository-url>
cd <project-folder>
{code}

---

h3. Build Project

Compile and install dependencies using Maven:

{code:bash}
mvn clean install
{code}

---

h3. Navigate to Decoder Module

Go to the module/package where the decoder is located:

{code:bash}
cd src/main/java/amex/iso8583
{code}

(Adjust the path depending on your project structure)

---

h3. Run Decode Runner

Locate the runner class (e.g. *DecodeRunner*) and execute it from your IDE.

Example:

{code:java}
public class DecodeRunner {

    public static void main(String[] args) {

        byte[] rawMessage = ...; // load from DB or file

        AmexDecoder decoder = new AmexDecoder();

        DecodedAmexResult<? extends AmexMessage> result =
                decoder.decode(rawMessage);

        DecodedPrettyPrinter.print(
                result,
                rawMessage,
                System.out,
                java.util.Arrays.asList("PAN", "P55")
        );
    }
}
{code}

---

h3. Notes

* Raw message is expected as byte[] (typically loaded from DB or file)
* No additional setup is required for default decoding
* Optional flags can be passed using DecodeOptions if needed