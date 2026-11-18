
# EMV & Gateway Integration with PAFI

## Overview

This documentation describes the integration of EMV and Gateway services with the Release Automation Framework (PAFI).

The integration extends PAFI's automated testing capabilities by providing reusable API clients, authentication management, database connectivity, scenario context tracking, and dedicated utilities for payment tokenisation testing.

It also introduces automated mechanisms for managing test configurations, controlling WireMock behaviour, and maintaining consistent test data across scenarios.

## Key Capabilities

### API Integration
- Reusable EMV and Gateway REST clients.
- Automated authentication and JWT management.
- JSON request templates and dynamic payload modification.
- Centralised request and response handling.

### Scenario Context
- Automatic tracking of API requests and responses.
- Access to previously executed operations.
- Storage and retrieval of scenario-specific data.
- Centralised access through the shared Singleton service holder.

### Database Integration
- Oracle Database integration for token and transaction validation.
- MongoDB integration for tokenisation configuration retrieval and verification.
- Database utilities for querying, mapping, and validating test data.

### Automated Token Cleanup
- Automatic token cleanup before and after test scenarios.
- Cucumber tag-driven database initialization and cleanup.
- Token cleanup by Token Owner ID and transaction correlation references.

### WireMock State Management
- Scenario-level WireMock activation and deactivation.
- Automatic preservation and restoration of the original state.
- Integration with Cucumber hooks and Spring Actuator.

### TGS Configuration Management
- Automated updates of tokenisation settings and digital wallet configuration.
- Comparison of requested configuration against MongoDB.
- Conditional waiting for configuration activation.
- Verification of the resulting configuration state.

## Documentation Structure

The following chapters describe the framework architecture, available components, and practical usage.

1. **Framework Overview**
2. **High-Level Architecture**
3. **Centralized Service Access & Singleton Management**
4. **API Client Architecture**
5. **Request & Response Management**
6. **Scenario Context**
7. **Oracle Database Integration**
8. **MongoDB Integration**
9. **Cucumber DataTable & JSON Integration**
10. **WireMock Integration & State Management**
11. **TGS Configuration Management & Validation**
12. **Logging & Error Handling**
13. **End-to-End Usage Examples**
