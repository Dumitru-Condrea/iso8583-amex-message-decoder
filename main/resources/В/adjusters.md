h1. Adjusters (Dynamic Schema)

h2. Overview

The decoder supports dynamic schema modifications through a concept called *Field Definition Adjusters*.

Adjusters allow the system to modify how fields are interpreted at runtime without changing the base message definitions.

This makes the decoder flexible and adaptable to variations in message formats across different flows, environments, or integrations.

---

h2. Purpose

Adjusters are used to:

* Adapt decoding logic based on runtime conditions
* Support multiple message variants within the same MTI
* Handle protocol inconsistencies between systems
* Enable backward compatibility with legacy formats
* Introduce conditional field behavior without duplicating schemas

---

h2. How It Works

1. A base MessageDefinition defines the default schema
2. During decoding, each field may have an associated adjuster
3. The adjuster receives DecodeOptions (flags)
4. Based on those flags, it can modify the field definition
5. The decoder uses the adjusted definition for parsing

---

h2. Key Characteristics

* Adjusters are optional
* If no flags are provided, the base schema is used unchanged
* Adjustments are applied only during decoding
* The final schema used is stored in the decoding result
* Adjusters are isolated per field (no global side effects)

---

h2. Examples of Adjustments

Adjusters can modify:

* Field structure (add/remove subfields)
* Field names
* Length definitions
* Encoding rules
* Presence of fields
* Order of subfields

---

h2. Design Benefits

* Separation of concerns (schema vs runtime behavior)
* High flexibility without modifying core logic
* Safe default behavior
* Easier support for multiple integrations
* Reduced code duplication

---

h2. When to Use Adjusters

Use adjusters when:

* The same field behaves differently across flows
* External systems send inconsistent formats
* You need to introduce feature flags into decoding
* A field structure depends on business context

Avoid adjusters when:
* The schema is static and consistent
* Behavior can be handled purely in mapping layer