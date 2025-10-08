# GopeText — Project Documentation

![Kotlin](https://img.shields.io/badge/Kotlin-1.9-blue?logo=kotlin)
![Android](https://img.shields.io/badge/Android-API%2024+-green?logo=android)
![Architecture](https://img.shields.io/badge/Architecture-MVP-orange)
![License](https://img.shields.io/badge/License-MIT-lightgrey)

## Overview

GopeText is an instant messaging mobile application for Android. The app consumes a REST API to register users, log in, list users, create chats, send/receive messages, and manage user sessions. The project prioritizes maintainability and testability through the MVP architecture, SOLID principles, and design patterns (Repository, Strategy, Result, Factory). Network error handling is unified with an `ApiResult` type and safe helpers for HTTP calls.

---

## Goals

- Provide 1-to-1 and group messaging connected to a REST API.
- Maintain a clear, decoupled, and scalable architecture.
- Centralize error and session handling.
- Avoid fragile UI: use ViewBinding and reusable validators.
- Facilitate unit and integration testing by isolating the data layer.

---

## Architecture

### Presentation Pattern: MVP

- **View**: Activities/Fragments. Render UI and delegate actions to the Presenter.
- **Presenter**: Orchestrates use cases; depends on repositories (abstractions).
- **Model**: Repositories and data models.

### SOLID Principles Applied

- **S** (Single Responsibility): UI, validation, network, and session storage are separated into dedicated components.
- **O** (Open/Closed): Repositories and Presenters can be extended without editing existing code.
- **L** (Liskov Substitution): Repository interfaces allow interchangeable implementations.
- **I** (Interface Segregation): Minimal contracts per feature (e.g., LoginContract, HomeContract).
- **D** (Dependency Inversion): Presenters depend on repository interfaces, not on Retrofit.

### Patterns

- **Repository**: Data layer per feature (AuthRepository, AccountRepository, ChatRepository, UsersRepository).
- **Strategy**: Reusable field validators (EmailValidator, PasswordValidator, etc.).
- **Result/Either**: `ApiResult` standardizes Success, HttpError, NetworkError.
- **Factory/Provider**: `ApiClient.createService<T>()` to build Retrofit services without exposing Retrofit.

---

## Folder Structure (Summary)

```
com.example.gopetext
├─ core
│  ├─ ApiResult.kt
│  └─ SafeApiCall.kt
├─ data
│  ├─ api
│  │  ├─ ApiClient.kt
│  │  ├─ AuthInterceptor.kt
│  │  ├─ AuthService.kt
│  │  └─ ChatService.kt
│  └─ repository
│     ├─ AuthRepository.kt
│     ├─ UserRegistrationRepository.kt
│     ├─ AccountRepository.kt
│     ├─ ChatRepository.kt
│     └─ UsersRepository.kt
├─ auth
│  ├─ login
│  │  ├─ LoginActivity.kt
│  │  ├─ LoginPresenter.kt
│  │  ├─ LoginContract.kt
│  │  └─ validation/Validators.kt
│  ├─ register
│  │  ├─ RegisterActivity.kt
│  │  ├─ RegisterPresenter.kt
│  │  ├─ RegisterContract.kt
│  │  └─ validation/RegisterValidators.kt
│  ├─ home
│  │  ├─ HomeActivity.kt
│  │  ├─ HomePresenter.kt
│  │  ├─ HomeContract.kt
│  │  └─ users
│  │     ├─ UsersFragment.kt
│  │     ├─ UsersPresenter.kt
│  │     ├─ UsersContract.kt
│  │     ├─ UsersAdapter.kt
│  │     └─ chat
│  │        ├─ ChatSingleActivity.kt
│  │        ├─ ChatSinglePresenter.kt
│  │        └─ ChatSingleContract.kt
└─ utils
   └─ Constants.kt
```

---

## Main Dependencies

- Kotlin + Coroutines
- Retrofit2 + GsonConverter
- OkHttp3
- AndroidX AppCompat, RecyclerView, BottomNavigation
- ViewBinding
- Glide

---

## Setup & Execution

### Prerequisites

- Updated Android Studio.
- JDK 17 (or Gradle required version).
- Device or emulator with API 24+.

### Variables & Constants

Set the base URL in `utils/Constants.kt`:

```kotlin
object Constants {
    const val BASE_URL = "https://your-domain-or-ip/"
    const val DEFAULT_SUCCESS = "Success"
    const val UNKNOWN_ERROR = "Unknown error"
    const val NETWORK_ERROR = "Network error: "
}
```

Enable **ViewBinding** in `app/build.gradle`:

```gradle
android {
    buildFeatures { viewBinding true }
}
```

### Build & Run

- Sync Gradle.
- Run the app on a device/emulator.

---

## Functional Modules

### Authentication

**Login**
- `LoginActivity` uses ViewBinding and validators (EmailValidator, PasswordValidator).
- `LoginPresenter` depends on AuthRepository and SessionManager.

**Register**
- `RegisterActivity` with validators (NonEmptyTextValidator, AgeRangeValidator, EmailValidator, PasswordValidator).
- `RegisterPresenter` depends on UserRegistrationRepository.

### Home

- `HomeActivity` manages bottom navigation.
- `HomePresenter` depends on AccountRepository and SessionManager.

### Users

- `UsersFragment` lists users with `UsersAdapter`.
- `UsersPresenter` depends on UsersRepository and ChatRepository.

### Chats

- `ChatSingleActivity` displays and sends messages; allows leaving groups.
- `ChatSinglePresenter` depends on ChatRepository and uses periodic refresh (5s).

---

## Network Layer

### ApiClient

- Initialize with `ApiClient.init(context)`.
- Services via `ApiClient.createService<T>()` or `ApiClient.getService()`.

### AuthInterceptor

- Adds `Authorization` if token exists.
- Avoids `Content-Type: application/json` in multipart requests.

### Error Handling

- `ApiResult.Success<T>(data)`  
- `ApiResult.HttpError(code, message)`  
- `ApiResult.NetworkError(message)`  

Helpers:  
- `safeApiCall { Response<T> }`  
- `safeBodyCall { T }`  

---

## Validation

- `EmailValidator`, `PasswordValidator`
- `NonEmptyTextValidator`, `AgeRangeValidator`

They return `String?` (error message or null if valid).

---

## Session Management

- `SessionManager` stores token and user_id.
- Interceptor adds token to requests.
- Logout clears local session; network errors do not block logout.

---

## Code Standards

- Descriptive English names.
- No comments in production code.
- Thin Presenters; logic in repositories.
- Conventional Commits style.

---

## Performance & Improvements

- Integrate DiffUtil in lists.
- Replace polling with WebSockets.

---

## Security

- Do not log tokens.
- Encapsulate Retrofit.
- Revoke token on server at logout if applicable.

---

## Unit test coverage

- Unit tests were performed on some classes of the project, thus covering 66% of the current version, with around 109 tests passed out of 133 tests.
  <img width="800" height="233" alt="image" src="https://github.com/user-attachments/assets/346008dd-8bb9-448e-9862-402544354aa3" />


---
