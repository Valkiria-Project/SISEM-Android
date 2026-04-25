# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build Commands

```bash
# Build specific variants
./gradlew assembleDebug          # Debug build (test.emergencias-sisem.co)
./gradlew assembleStaging        # Staging build (api.emergencias.saludcapital.gov.co)
./gradlew assemblePreProd        # Pre-production build (mobile-preprod.sisem.co)
./gradlew assembleRelease        # Release build (requires keystore.properties)

# Run all unit tests
./gradlew test

# Run specific test class
./gradlew testDebugUnitTest --tests "com.skgtecnologia.sisem.domain.operation.usecases.GetOperationConfigTest"

# Code quality
./gradlew ktlintCheck            # Check code style
./gradlew ktlintFormat           # Auto-fix code style
./gradlew detekt                 # Static analysis

# Install on device
adb install -r app/build/outputs/apk/staging/com.skgtecnologia.sisem-v*.apk
```

## Architecture

**Clean Architecture + MVVM** with feature-based organization:

```
app/src/main/java/com/skgtecnologia/sisem/
├── commons/          # Shared utilities, extensions, location services
├── data/             # Repository implementations, APIs, local DB
│   └── <feature>/
│       ├── remote/   # Retrofit APIs and DTOs
│       ├── cache/    # Room DAOs and entities
│       └── *RepositoryImpl.kt
├── domain/           # Business logic layer
│   └── <feature>/
│       ├── usecases/ # Single-purpose use cases
│       ├── model/    # Domain models
│       └── *Repository.kt (interface)
├── di/               # Hilt modules per feature
└── ui/               # Compose screens and ViewModels
    └── <feature>/
        ├── *Screen.kt
        ├── *ViewModel.kt
        └── *UiState.kt
```

**uicomponents/** - Shared Compose UI library with 30+ reusable components and design system.

## Key Patterns

- **Single Activity**: `MainActivity.kt` hosts all Compose navigation
- **Navigation**: Type-safe routes via sealed classes in `ui/navigation/`
- **API Calls**: `NetworkApi.apiCall{}` wrapper handles errors and maps to `BannerModel`
- **State Management**: `MutableStateFlow` in ViewModels, collected with `collectAsStateWithLifecycle()`
- **DI**: Feature modules in `di/` (e.g., `auth/AuthNetworkModule.kt`, `auth/AuthRepositoryModule.kt`)

## Build Variants

| Variant | Base URL | Use Case |
|---------|----------|----------|
| debug | test.emergencias-sisem.co | Development |
| staging | api.emergencias.saludcapital.gov.co | QA testing |
| preProd | mobile-preprod.sisem.co | Pre-release |
| release | api.emergencias.saludcapital.gov.co | Production |

## Testing

- **Unit tests**: `app/src/test/` using JUnit 4 + MockK + Robolectric
- **UI tests**: `app/src/androidTest/` using Compose testing
- **Screenshot tests**: Paparazzi in CI workflow

## Code Quality

Git hooks auto-run on commit/push:
- **Pre-commit**: Ktlint formatting
- **Pre-push**: Detekt + Ktlint checks

Detekt config: `config/detekt/detekt.yml` (zero tolerance for issues)

## Important Files

- `app/build.gradle.kts` - Build configuration and environment URLs
- `gradle/libs.versions.toml` - Centralized dependency versions
- `ui/navigation/SisemNavGraph.kt` - Main navigation structure
- `data/remote/api/NetworkApi.kt` - API call wrapper with error handling
- `di/ApplicationModule.kt` - Core Hilt bindings
