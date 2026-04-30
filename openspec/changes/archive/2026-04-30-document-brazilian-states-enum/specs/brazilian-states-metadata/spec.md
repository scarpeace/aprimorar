## ADDED Requirements

### Requirement: Enum Documentation

The `BrazilianStates` enum **SHALL** be documented using OpenAPI `@Schema` annotations to provide clear descriptions and examples in the generated API documentation.

#### Scenario: Metadata Generation

- **WHEN** the backend code is processed by the OpenAPI generator
- **THEN** the `BrazilianStates` enum should include a schema description "Estados Brasileiros"
- **AND** the generated documentation should reflect the display names and constants correctly.

#### Scenario: Kubb Synchronization

- **WHEN** Kubb runs against the updated OpenAPI spec
- **THEN** it should be able to generate a TypeScript enum or type definition for `BrazilianStates` based on the provided schema.
