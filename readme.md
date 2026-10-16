Hi team,

I'll be off for the next three days, so I'm sharing a quick reference guide for the recently implemented Cucumber steps and tags used for TGS, EMV, and DB token validation in PAFI.

The descriptions below explain what each step does, which parameters it accepts, and when it can be useful.

Token Management and TGS Steps

1. Set Token Owner ID

Accepts a String parameter and stores the provided tokenOwnerId in TestData for subsequent test operations.

2. Toggle Actuator Configuration

Accepts ON / OFF (case-insensitive), mapped to true / false, and sends the corresponding configuration update request to Spring Actuator.

3. Change Token Owner State in TGS

Accepts a card scheme (AMEX, VISA, or MASTERCARD, uppercase only) and a DataTable containing the request body modifications.

Sends a changeTokenOwnerState request to TGS using the provided values.

Example:

| $.emvSettings.exposeTokenToMerchant | true |
| $.emvSettings.emvTokenisationEnabled | true |

4. Toggle Merchant Tokenisation Settings

Accepts two parameters:

Card scheme: AMEX / VISA / MASTERCARD (uppercase only).

Toggle state: ON / OFF (case-insensitive), mapped to true / false.

Sends a TGS request to update the merchant's small tokenisation toggle and token exposure setting.

Apple Pay and Google Pay settings remain unchanged.

5. Toggle Apple Pay and Google Pay Settings

Accepts two parameters:

Card scheme: AMEX / VISA / MASTERCARD (uppercase only).

Toggle state: ON / OFF (case-insensitive), mapped to true / false.

Sends a TGS request to enable or disable Apple Pay and Google Pay for the merchant.

6. Get or Create a Token in TGS

Accepts a card scheme (amex, visa, or mastercard, lowercase only) and a DataTable containing request body modifications.

Sends a getOrCreateToken request to TGS.

Example:

| $.payload.number       | 375000000004807 |
| $.pciToken             | 3750ffdgbdc4807 |
| $.isCryptogramRequired | true            |

7. Change Token State and Validate DB Records

Accepts two parameters:

Action: resume / suspend / delete (case-insensitive).

Expected token status: active / expired / suspended / deleted (case-insensitive).

Sends a changeTokenState request to TGS and additionally validates the resulting token status and corresponding token event in the database.

Transaction and Host Message Validation

8. Validate PAN or Network Token Usage

Accepts PAN / NETWORK TOKEN (case-insensitive).

Verifies whether the transaction uses the expected payment credential in both the transaction response and the Host Message.

When NETWORK TOKEN is selected, additional validation is performed against the TGS getToken response.

9. Validate CID_CODE Mapping in Host Message

Accepts one of the following values (case-insensitive):

CVV — verifies that a non-tokenised transaction uses the CVV from the original request rather than a cryptogram.

RANDOM CRYPTOGRAM — verifies that the Host Message contains a random cryptogram value instead of the transaction CVV.

LAST CRYPTOGRAM — verifies that the Host Message reuses the previous cryptogram rather than the CVV. This is applicable to refund transactions, where a new cryptogram is not generated.

ABSENT — verifies that the cryptogram is absent from the Host Message.

The validation checks the mapping of [P53.0] CID_CODE based on the selected expectation.

Database Token Event Validation

10. Retrieve and Store Token Events

Queries the TOKEN_EVENTS table using the latest transaction's correlationId, retrieves all matching events, and automatically stores the resulting list in the scenario context.

Context tracking note: All DB query results are automatically stored in the context. The same applies to TGS and EMV API calls.

You can find the corresponding context key in the request logs under CONTEXT KEY. This indicates which key was used to store the request/response data or DB result for later retrieval.

11. Verify That No Token Events Were Created

Queries TOKEN_EVENTS using the latest transaction's correlationId and verifies that no matching events exist.

This is particularly useful for non-tokenised transactions, where no token events are expected.

12. Validate Token Event Types

This is a parameterised step that supports multiple filtering and assertion modes.

Parameter 1 — Event filtering mode:

latest transaction — retrieves all historical events associated with the token used in the latest transaction.

latest transaction by date — retrieves the token events but filters them for validation using the date and time of the latest transaction.

The second option is particularly useful for updateWebPayments, MIT, and similar flows, where we only want to validate events created after the most recent transaction or update.

Both values are case-insensitive.

Parameter 2 — Event matching mode:

are — verifies that all expected event types are present among the actual events. Additional event types are allowed.

are only — verifies that the actual event types contain only the expected types, with all expected types present.

not — verifies that none of the specified event types are present.

was not created — verifies that no token events exist.

All values are case-insensitive.

The step also accepts a DataTable containing the expected event types.

Example scenarios (illustrative):

Then I verify that the token events for the latest transaction - are:
  | TOKEN_CREATED |
  | TOKEN_UPDATED |

Then I verify that the token events for the latest transaction - are only:
  | TOKEN_CREATED |

Then I verify that the token events for the latest transaction by date - not:
  | TOKEN_DELETED |

Then I verify that the token events for the latest transaction by date - was not created:

The event type names above are illustrative; use the actual supported event types from the framework.

13. Verify the Absence of Token Events Without a DataTable

Accepts one parameter:

latest transaction

latest transaction by date

Both are case-insensitive.

Verifies that no token events exist, either across the token's entire event history or within the date-filtered results for the latest transaction.

This is effectively a simplified version of the was not created assertion from step 12, allowing the same validation without providing a DataTable.

Functional Cucumber Tags

The following tags are also available to simplify scenario setup, state management, and cleanup.

1. WireMock State Management

@wiremock.active:true
@wiremock.active:false

These tags control the WireMock activation state through Spring Actuator.

Before the scenario, the hook:

Retrieves and saves the original WireMock state.

Applies the requested true or false value.

Validates that the expected state has been applied.

After the scenario, the hook restores the original WireMock state, preventing configuration changes from affecting subsequent scenarios.

2. Database Connection and Token Cleanup

@DB
@DB:cleanup_tokens

@DB — automatically initialises the database connection, so there is no need to establish it manually in the test.

@DB:cleanup_tokens — enables token cleanup around the scenario:

Before the scenario: intended to remove all existing tokens associated with the configured tokenOwnerId. This functionality is currently disabled.

After the scenario: removes tokens created by the transactions executed during the scenario.

This helps keep the database clean and reduces interference between test executions.

Additional Note: Token Owner ID Configuration

For some clients, a default tokenOwnerId is already configured in AWS_config.properties.

During test execution, this value is automatically loaded into TestData, so it does not need to be explicitly defined in every scenario.

If a different Token Owner ID is required, it can be overridden using step 1.

Hopefully, this helps when writing new tokenisation scenarios or extending the existing ones. The steps are designed to be reusable across different transaction flows while keeping the Gherkin scenarios concise and readable.

Feel free to use these examples as a reference while I'm away.
