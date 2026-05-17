# NIT3213 Final Assignment - Android Application

## Overview

This project is an Android application developed for the NIT3213 Final Assignment.  
The application demonstrates API integration, dependency injection, RecyclerView-based data display, ViewModel architecture, repository pattern, and unit testing.

The app contains three main screens:

1. Login Screen
2. Dashboard Screen
3. Details Screen

## Features

- Login authentication using the NIT3213 API
- Dashboard data retrieval using the keypass returned from login
- RecyclerView list display for dashboard entities
- Details screen showing all selected entity information, including description
- Retrofit and Moshi for API communication and JSON parsing
- Hilt for dependency injection
- ViewModel and LiveData for UI state management
- Unit tests for LoginViewModel and DashboardViewModel

## API Information

Base URL:

```text
https://nit3213api.onrender.com/
```

Login endpoint:
`POST /sydney/auth`

Dashboard endpoint:
`GET /dashboard/{keypass}`

### Login Credentials Used for Testing
- **Username**: s8115784
- **Password**: Justin

## Tech Stack
- Kotlin
- Android XML layouts
- Retrofit
- Moshi
- OkHttp
- Hilt
- ViewModel
- LiveData
- Coroutines
- RecyclerView
- JUnit

## Project Structure
`app/src/main/java/com/vu/androidbasicapp/`
```text
├── data/
│   ├── model/
│   ├── remote/
│   └── repository/
├── di/
├── ui/
│   ├── login/
│   ├── dashboard/
│   └── details/
└── MyBaseApplication.kt
```

## How to Build and Run
1. **Clone the repository**:
   ```bash
   git clone https://github.com/JustinCoKA/NIT3213-ASS2.git
   ```
2. Open the project in Android Studio.
3. Sync Gradle.
4. **Run unit tests**:
   ```bash
   ./gradlew clean test
   ```
5. **Build debug APK**:
   ```bash
   ./gradlew assembleDebug
   ```
6. Run the app on an Android emulator or physical device.

## Testing
The project includes unit tests for critical ViewModel logic:
- `LoginViewModelTest`
- `DashboardViewModelTest`

Run tests using:
```bash
./gradlew test
```

## Notes
- The application uses the Sydney class endpoint for authentication.
- The username is the student ID including 's', and the password is the student's first name.
