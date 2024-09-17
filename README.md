[![Maven Central](http://img.shields.io/maven-central/v/de.halfbit/logger.svg)](https://central.sonatype.com/artifact/de.halfbit/logger)
![maintenance-status](https://img.shields.io/badge/maintenance-actively--developed-brightgreen.svg)

# 🪵 Logger

Minimalistic and concise Logger for Kotlin Multiplatform from one of my projects,
shared with the awesome Kotlin community.

![Architecture diagram](https://www.plantuml.com/plantuml/svg/bPFFIWCn48VlUOeX9mljWbSHQL6HIWMnGW_Y8SrErzXaiYIfTQbzTyd-AHjT3zwQRxvCVfbcfpu85tP6CxzLL0ed39H2RaM1or1f18So22eqiZUL8w0fGmK6pHgTRv0FwD26gcmLgPpKo9ZKmdlW-Tdpx6u41-0tkDuL78uSX8TwqJdgvCnfOpib_kwC_SbO2KUAJdtYifCmIcF0JubbK9Pg_zOvwpgxNdIwjaMpOM6B1NwW7aEMPQCeHiHOdH4cBxaD0VFNIQHm0LSi9XlYCSmGhuyTAZ150Tr6I8negMWB1mO0S11h7voGuGX_n6H7njf_2fJrxRxcQdFhstPzIr3khCeJSzQm0LjQOombygDJ53H5iqSVq5XNFIdwrU8zJSMlBNchiJsiddEAxdr_-rvuy-yMA-5DqwO5SFcLPKdqf92cJslfS99Q7F4W_tcXuMgIDIh0ptd_WN3Yq5hrGvaYvV4h_GO0)

# Usage

```kotlin
// Initialize
class App : Application() {
    override fun onCreate() {
        initializeLogger {
            registerPrintlnSink() // used by default if no sinks are configured
            registerMemoryRingSink()
            registerAndroidLogSink()
            loggableLevel = if (isDebugBuild) Everything else InfoAndSevere
        }
    }
}

// Use logger
private const val TAG = "Reader"

class Reader(private val bufferSize: Int) {

    init {
        i(TAG) { "created: size=$bufferSize" } // info
        d(TAG) { "created: size=$bufferSize" } // debug
    }

    suspend fun readData() {
        try {
            httpClient.get("read-data")
        } catch (e: Exception) {
            w(TAG, e) { "data reading failed" } // warning
            e(TAG, e) // error
        }
    }
}
```

## iOS usage

```swift
init() {
    LogKt.initializeLogger { builder in
        builder.registerIosLogSink(logPrinter: LogPrinterCompanion.shared.Default)
    }
}

private let TAG = "SampleApp"
LogKt.d(tag: TAG) { "debug message" }
```

![iOS log](https://raw.githubusercontent.com/sergejsha/logger/master/documentation/examples/iOS.png)

## Android usage

```kotlin
override fun onCreate() {
    initializeLogger {
        registerAndroidLogSink()
    }
}
```

![Android log](https://raw.githubusercontent.com/sergejsha/logger/master/documentation/examples/android.png)

## Desktop usage

```kotlin
fun main() {
    initializeLogger {
        registerPrintlnSink()
    }
}
```

![Desktop log](https://raw.githubusercontent.com/sergejsha/logger/master/documentation/examples/desktop.png)

## Browser usage

```kotlin
fun main() {
    initializeLogger {
        registerConsoleLogSink()
    }
}
```

![Browser log](https://raw.githubusercontent.com/sergejsha/logger/master/documentation/examples/jsBrowser.png)

# How to

## Disable logger for a class

```kotlin
// Create a named tag instance instead of a string-tag
private val tag = namedTag("ScaleAnimation")

// Use the tag in log-functions
d(tag) { "animation started" }
d(tag) { "scale: ${scale.value}" }
...

// Set a loggable level to disable unwanted logs for this tag
private val tag = namedTag("ScaleAnimation", LoggableLevel.Nothing)
```

## Use class name for tags

```kotlin
class Reader {
    private val tag = classTag<Reader>()

    init {
        d(tag) { "created" }
    }
}
```

# How to use it in my project?

In `gradle/libs.versions.toml`

```toml
[versions]
kotlin = "2.0.20"
logger = "0.7"

[libraries]
logger = { module = "de.halfbit:logger", version.ref = "logger" }

[plugins]
kotlin-multiplatform = { id = "org.jetbrains.kotlin.multiplatform", version.ref = "kotlin" }
```

In `shared/build.gradle.kts`

```kotlin
plugins {
    alias(libs.plugins.kotlin.multiplatform)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.logger)
        }
    }
}
```

# Publish to maven Central

1. Bump version in `root.publication.gradle.kts`
2. `./gradlew clean build publishAllPublicationsToCentralRepository`

# Publish to local maven

1. Set `X.X-SNAPSHOT` version in `root.publication.gradle.kts`
2. `./gradlew clean build publishToMavenLocal`

# License

```
Copyright 2024 Sergej Shafarenka, www.halfbit.de

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
