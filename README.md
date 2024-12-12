# Turo Take-Home App

## Architecture

This app was implemented by following CLEAN architecture:

1. Data layer: Retrofit + Gson
2. Domain layer: Coroutine
3. UI layer:
* MVVM
* Flow
* Jetpack Compose + Navigation
* Coil
4. Dependency Injection: Hilt

## Features Implemented:
1. Search using address or zip code
2. Search using GPS (current location)
3. Pagination support (infinite scrolling)
4. Custom font

## Functionalities Improvements:
1. Testing: 
* Unit test
* Instrumentation test
2. Security: SSL Pinning
3. Memory leak monitoring: LeakCanary
4. CI/CD: Github action

## Future Improvements:
1. Testing: Espresso
2. Health monitoring: Crashlytics / Sentry
3. Analytics: Firebase Analytics / Segment
4. RemoteConfig: Firebase RemoteConfig / Optimizely
5. CI/CD:
* Static code analysis: SonarCube
6. App update (force / optional)
  
