# EMV & Gateway Integration with PAFI

## Overview

This section documents the integration of EMV and Gateway services with the Release Automation Framework (PAFI).

The integration extends PAFI's automated testing capabilities by providing reusable API clients, authentication management, database access, scenario context tracking, and dedicated utilities for validating payment tokenisation workflows.

It also includes mechanisms for managing test configurations, controlling WireMock behaviour, and automatically cleaning up test tokens.

## Key Capabilities

- **API Integration** — EMV and Gateway REST clients with automated authentication and reusable request handling.
- **Scenario Context** — Centralised access to previously executed API requests, responses, and scenario-specific data.
- **Database Integration** — Oracle and MongoDB clients for test data access, token-event verification, and configuration validation.
- **Automated Token Cleanup** — Cucumber tag-driven cleanup before and after scenarios.
- **WireMock Management** — Scenario-level management of WireMock state.
- **TGS Configuration Management** — Automated tokenisation configuration updates with MongoDB verification and conditional waiting.

## Documentation
