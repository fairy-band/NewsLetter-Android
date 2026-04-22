# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**쏙 (Soak)** — 개발자 대상 뉴스레터 AI 큐레이션 앱

GeekNews, Android Weekly, 개발 블로그 등 개발자가 구독할 만한 뉴스레터를 수집하고, AI가 매일 7개의 아티클을 개인화하여 큐레이팅해 준다. 탐색(Explore) 탭에서는 개인화되지 않은 전체 뉴스레터 목록도 제공한다.

주요 화면 구성:
- **Home** — AI가 선별한 오늘의 추천 아티클 7개 (개인화)
- **Explore** — 전체 뉴스레터 탐색 (비개인화), 상세 보기(`ExploreDetail`)
- **WebView** — 아티클 본문 표시
- **Setting** — 개인 설정 및 서비스 설정

Firebase Remote Config로 A/B 테스트 및 원격 피처 플래그를 운용하고, FCM으로 새 큐레이션 알림을 발송한다. Kakao SDK는 로그인 및 링크 공유에 사용된다.

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