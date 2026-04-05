# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build Commands

```bash
./gradlew assembleDebug              # Debug APK
./gradlew assembleRelease            # Release APK (requires keystore.properties)
./gradlew bundleRelease              # Release AAB
./gradlew testDebugUnitTest          # Run all unit tests
./gradlew :presentation:testDebugUnitTest  # Run tests for a specific module
```

Release builds require `keystore.properties` and a JKS keystore file configured locally.

CI/CD deployment:
- Firebase App Distribution: auto-triggered on merge to `develop`
- Play Store internal track: triggered manually via GitHub Actions with `version_code` and `version_name` parameters, or via `fastlane android deploy version_code:X version_name:Y`

## Architecture

Clean Architecture with MVVM, organized as a Gradle multi-module project:

```
app/           → entry point, wires up Koin DI modules
presentation/  → Jetpack Compose UI, ViewModels, navigation
domain/        → use cases (business logic, no Android deps)
data/          → repositories, Retrofit services, DataStore
core/          → shared Compose components, Timber logging
```

**Dependency direction:** `app → presentation → domain ← data → core`

### Presentation Layer

Features live under `presentation/src/main/java/com/fairyband/soak/presentation/`:
- Each feature has a `[Feature]ViewModel.kt`, `[Feature]Screen.kt`, and optionally a `[Feature]State.kt`
- Navigation is managed with Jetpack Navigation 3 (`androidx.navigation3`)
- Deep links use the `soak://` scheme and Kakao link scheme

### Data Layer

- `repository/` — interfaces; `repositoryimpl/` — implementations
- `remote/service/` — Retrofit API definitions
- `local/` — DataStore-based persistence (replaces SharedPreferences)
- `datasource/` — data source abstractions
- API base URL is injected via `BuildConfig.BASE_URL`

### Dependency Injection

Koin with KSP annotations. Five modules initialized in `SoakApplication`:
`DataModule`, `DomainModule`, `PresentationModule`, `RetrofitModule`, `ServiceModule`

## Key Libraries

- **UI:** Jetpack Compose (BOM 2024.09.00), Material 3
- **DI:** Koin 4.1.0 with KSP
- **Networking:** Retrofit 3, OkHttp 5, Kotlinx Serialization
- **State:** Kotlin Coroutines 1.10.2, StateFlow/SharedFlow
- **Storage:** DataStore Preferences
- **Firebase:** Analytics, Crashlytics, FCM, Remote Config, App Distribution
- **Auth/Share:** Kakao SDK 2.20.1
- **Testing:** Kotest 6.0.3 + JUnit 5

All versions are managed in `gradle/libs.versions.toml`.

## Configuration Files Required Locally

These are provided via base64-encoded GitHub Secrets in CI and must be created manually for local builds:
- `local.properties` — `NATIVE_APP_KEY` (Kakao), `BASE_URL`
- `keystore.properties` — signing config for release builds
- `google-services.json` — Firebase configuration (place in `app/`)

## Naming Conventions

- ViewModels: `[Feature]ViewModel`
- State classes: `[Feature]State` or `[Feature]DisplayState`
- Repository interfaces: `[Entity]Repository` (in `repository/`)
- Repository implementations: `[Entity]RepositoryImpl` (in `repositoryimpl/`)
- Use cases: `[Action]UseCase`
- API services: `[Entity]Service`