h1. AMEX ISO8583 Decoder

h2. Overview

The AMEX ISO8583 Decoder is a flexible, schema-driven component designed to parse and interpret AMEX ISO8583 messages into structured, readable, and strongly-typed data models.

It is built to support a wide range of message formats and variations, enabling consistent processing across different transaction flows while maintaining high transparency for debugging and analysis.

The decoder is designed with extensibility in mind, allowing dynamic behavior changes without modifying core logic.

---

h2. Key Capabilities

* Supports multiple AMEX message types (e.g. 1100, 1110, 1420, etc.)
* Converts raw byte messages into structured Java objects
* Handles complex and nested field structures
* Allows dynamic schema adjustments based on runtime flags
* Provides detailed debug output for troubleshooting and validation
* Works with both strict and flexible message formats

---

h2. Design Principles

* *Schema-driven* — decoding is based on predefined message definitions
* *Extensible* — behavior can be modified without changing core logic
* *Non-intrusive* — default behavior remains unchanged if no options are provided
* *Transparent* — all decoded data is accessible for debugging and validation
* *Test-friendly* — suitable for both automated tests and manual analysis

---

h2. Typical Use Cases

* Transaction message parsing and validation
* Integration testing of payment flows
* Debugging production issues
* Reverse-engineering message formats
* Supporting multiple protocol variants

---

h2. Navigation

Use the sections below to explore the decoder:

* [Architecture Overview]
* [Quick Start Guide]
* [Decoder Usage]
* [Message Definitions]
* [Adjusters (Dynamic Schema)]
* [Pretty Printer & Debugging]
* [Best Practices]

---

h2. Summary

The AMEX ISO8583 Decoder provides a robust and extensible foundation for working with ISO8583 messages, balancing strict protocol handling with the flexibility required for real-world integrations.