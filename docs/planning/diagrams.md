# Diagrams

Shared Mermaid diagrams for planning, architecture, and feature discussions.

## System architecture

```mermaid
flowchart TB
    subgraph Client["Frontend Client"]
        React["React SPA\nVite"]
    end

    subgraph Backend["Backend API"]
        API[("Spring Boot\nREST API")]
        SVC[Service Layer]
        MAPPER["Mapper Layer"]
    end

    subgraph Database["Data Layer"]
        JPA["JPA/Hibernate"]
        FLY["Flyway\nMigrations"]
        DB[("PostgreSQL\n15")]
    end

    React -->|HTTP/REST| API
    API --> SVC
    SVC --> MAPPER
    MAPPER --> JPA
    JPA --> FLY
    FLY --> DB
```

## Core domain relationships

```mermaid
erDiagram
    PARENT ||--o{ STUDENT : guardian_for
    STUDENT ||--|| ADDRESS : has
    EVENT }o--|| STUDENT : scheduled_for
    EVENT }o--|| EMPLOYEE : taught_by
```

## Student registration flow

```mermaid
flowchart TD
    A[Start] --> B[Admin opens student form]
    B --> C[Fill student details]
    C --> D[Fill address]
    D --> E[Fill or select parent]
    E --> F{Validation passes?}
    F -->|No| C
    F -->|Yes| G[Submit form]
    G --> H[API validates request]
    H --> I{Valid?}
    I -->|No| J[Return 400 with errors]
    J --> C
    I -->|Yes| K[Save in database]
    K --> L[Return 201]
    L --> M[End]
```

## Event scheduling flow

```mermaid
flowchart TD
    A[Start] --> B[Open event form]
    B --> C[Select student]
    C --> D[Select employee]
    D --> E[Set start]
    E --> F[Set end]
    F --> G[Set price]
    G --> H[Set payment]
    H --> I{Validation passes?}
    I -->|No| B
    I -->|Yes| J[Submit form]
    J --> K[API validates]
    K --> L{Valid?}
    L -->|No| M[Return 400]
    M --> B
    L -->|Yes| N[Save event]
    N --> O[Optional calendar sync]
    O --> P[Return 201]
    P --> Q[End]
```

## Authentication flow (planned)

```mermaid
flowchart TD
    A[Open login page] --> B[Enter credentials]
    B --> C[Submit login]
    C --> D{Valid credentials?}
    D -->|No| E[Return 401]
    E --> A
    D -->|Yes| F[Generate token]
    F --> G[Return token]
    G --> H[Client stores token]
    H --> I[Redirect to dashboard]
    I --> J[Next requests include token]
    J --> K{Token valid?}
    K -->|No| L[Return 401]
    L --> A
    K -->|Yes| M[Process request]
    M --> N[Return response]
```
