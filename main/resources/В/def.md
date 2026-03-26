h1. Message Definitions

h2. Overview

Message Definitions describe the structure of ISO8583 messages for each MTI.

They define how raw message data should be interpreted, including field layout, encoding rules, and nested structures.

Message Definitions are the foundation of the decoder and enable consistent parsing across different message types.

---

h2. Purpose

Message Definitions are responsible for:

* Defining which fields are present in a message
* Specifying encoding and length rules
* Describing composite fields and subfields
* Providing structure for typed message mapping
* Enabling schema-driven decoding

---

h2. Structure

Each Message Definition includes:

* MTI (message type)
* Field definitions (by field number)
* Encoding and length rules
* Optional composite field structures
* Mapping logic to typed message objects

---

h2. Field Types

Fields can be defined as:

* *Primitive Fields*  
  Simple fields with fixed or variable length

* *Composite Fields*  
  Fields containing multiple subfields

---

h2. Composite Fields

Composite fields allow:

* Nested structures
* Multiple subfield definitions
* Independent decoding rules per subfield

They are used for complex data blocks where a single field contains structured content.

---

h2. Mapping Layer

Each Message Definition is associated with a mapper that converts decoded data into a typed Java object.

This ensures:

* Strong typing for business logic
* Clear separation between parsing and usage
* Easier testing and validation

---

h2. Key Characteristics

* Schema-driven approach (no hardcoded parsing)
* Fully extensible
* Supports multiple MTIs
* Independent per message type
* Compatible with dynamic adjustments (via adjusters)

---

h2. Design Benefits

* Clear separation of parsing logic and business logic
* Reusable schema definitions
* Easy onboarding for new message types
* Improved maintainability
* Reduced parsing errors

---

h2. When to Modify Message Definitions

Modify definitions when:

* Adding support for a new MTI
* Introducing new fields or subfields
* Updating encoding or length rules
* Aligning with external system changes

Avoid modifying definitions when:
* Behavior is conditional → use adjusters instead
* Logic belongs to business layer → use mapper