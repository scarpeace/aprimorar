## Why

Currently, the `BrazilianStates` enum in the backend is not documented with OpenAPI/Swagger annotations. This prevents our frontend code generator (Kubb) from automatically generating a synchronized TypeScript enum, leading to manual duplication and potential sync issues.

## What Changes

- Add OpenAPI `@Schema` annotations to `BrazilianStates.java`.
- Add Javadoc documentation to the enum.
- (Implicitly) Enable Kubb to generate the corresponding TypeScript types.

## Capabilities

### New Capabilities
- `brazilian-states-metadata`: Improved metadata for Brazilian states to support frontend code generation.

### Modified Capabilities

## Impact

- `server/api-aprimorar/src/main/java/com/aprimorar/api/enums/BrazilianStates.java`: Add annotations and documentation.
