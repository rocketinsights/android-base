# Rocket's Kotlin Android Architecture Components Example

This is the first version of a mock application that hopefully will evolve into the base project for Rocket's Android application.

Important branches:
- main - This branch represents the current state of the project. It's under regular maintenance. Use stable version of Android Studio to open the project.
- canary - This branch is for trying out Android preview features. Some features might be merged from this branch to master branch in the future. It is not under regular maintenance. Use canary version of Android Studio to open the project.

List of functionalities:
- REST API integration - [Retrofit](https://github.com/square/retrofit) library is used as a type-safe HTTP client. [Suspending functions](https://kotlinlang.org/docs/composing-suspending-functions.html#sequential-by-default) are used in Retrofit interfaces.
- Saving data locally - [Room](https://developer.android.com/training/data-storage/room) persistence library provides an abstraction layer over SQLite to allow database access. [Suspending functions](https://kotlinlang.org/docs/composing-suspending-functions.html#sequential-by-default) and [Flows](https://kotlinlang.org/docs/flow.html#flows) are used in Room DAO-s.
- Dependency injection - [Koin](https://insert-koin.io/) is used as a dependency injection framework.
- Navigation - [Android Jetpack's Navigation component](https://developer.android.com/guide/navigation) is used to implement navigation between fragments. Nav graph contains splash screen, main graph and auth graph. Auth graph represents sign-in flow, and main graph represents main application flow.
- Splash screen - SplashFragment contains Rocket Insights logo.
- Login screen - LoginFragment contains welcome message and login button which calls [FirebaseUI](https://firebase.google.com/docs/auth/android/firebaseui) sign-in flow.

[![Bitrise Build Status](https://app.bitrise.io/app/edf2965e90d6ca81/status.svg?token=M9TjJbSh1cmaUfFqzBkEUg&branch=master)](https://app.bitrise.io/app/edf2965e90d6ca81)

[![CircleCI Build Status](https://circleci.com/gh/rocketinsights/android-base.svg?style=svg&circle-token=bd395430a4c3e2741c39f8b305be451bf1655e15)](https://circleci.com/gh/rocketinsights/android-base)

[![App Center Build Status](https://build.appcenter.ms/v0.1/apps/72d0a1ef-6191-4f2e-b136-35f445fa383f/branches/master/badge)](https://appcenter.ms/orgs/Rocket-Insights/apps/Base-Android-App/build)

[![Travis Build Status](https://travis-ci.com/rocketinsights/android-base.svg?token=HUkRE8RunPYqyTqAocsA&branch=master)](https://travis-ci.com/rocketinsights/android-base)

![GitHub Actions Build Status](https://github.com/rocketinsights/android-base/actions/workflows/pull_request.yml/badge.svg)

## Application Installation
Below are the instructions on how to install the Android app.

### Enable Installation from Unknown Sources
First, for any given device, it'll need to have installation from unknown sources enabled.

1. Open the Settings app.
2. Tap on Security.
3. Scroll to Unknown sources and turn the setting on.

Next, there are a few ways to get the latest application.

### Via qa@yourdomainname.com
Whenever the newest build is available, (Bitrise|CirchleCI|AppCenter|Travis) emails qa@yourdomainname.com with a link to the latest apk. Tap on that link from the device. Once it has downloaded, tapping on the app should show a prompt to confirm the installation of the application. Occasionally, depending on device or manufacturer, you need to go to the Downloads application and install from there. If there's an issue installing from the mail application, try that.

### Via Slack Channel
Whenever a new build is available, the ﻿#androidslackchannel﻿ slack channel has an automated message sent to it. Open the link in that message from the device and follow the above instructions.

### Request a Specific Build
If you want a specific build, let me (Android developer) know and I can send you that build directly on (Bitrise|CirchleCI|AppCenter|Travis). From there, you can follow the directions for qa@yourdomainname.com.

### Via Android Studio
If you get set up with Android Studio﻿ and the GitHub repo﻿ on your local machine, you'll be able to install it directly from there. If you need help with this, feel free to let me (again, Android developer) know and I'd be happy to walk you through it.
