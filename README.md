[![Maven Central](http://img.shields.io/maven-central/v/de.halfbit/logger.svg)](https://central.sonatype.com/artifact/de.halfbit/logger)

# Logger

Minimalistic Logger for Kotlin Multiplatform

# Usage

```kotlin

// Initialize
class App : GamineApp, Application() {
    override fun onCreate() {
        initializeLogger {
            registerAndroidLogSink()
            logLevel = if (isDebugBuild) LogLevel.Debug else LogLevel.Info
        }
    }
}

// Use
init {
    i(TAG) { "created: size=${bytes?.size}" } // info
    d(TAG) { "created: size=${bytes?.size}" } // debug

    try {
        httpClient.post("ReadData")
    } catch (e: Exception) {
        w(TAG, e) { "ReadData failed" } // warning
        e(TAG, e) { "ReadData failed" } // error
    }
}

```

# Dependencies

In `gradle/libs.versions.toml`

```toml
[versions]
kotlin = "2.0.0"
logger = "0.1"

[libraries]
csv = { module = "de.halfbit:logger", version.ref = "logger" }

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

# Release

1. Bump version in `build.gradle.kts` of the root project
2. `./gradlew clean build publishAllPublicationsToCentralRepository`

# Release Notes

- 0.1 Initial release

# License

```
Copyright 2024 Sergej Shafarenka, www.halfbit.de

You are free to
  - copy and redistribute the material in any medium or format;
  - remix, transform, and build upon the material;
  - use the material or its derivative for personal or commercial purposes.

Any distributed derivative work containing this material or parts 
of it must have this copyright attribution notices.

The material is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES 
OR CONDITIONS OF ANY KIND, either express or implied.

Contact the developer if you want to use the material under a 
different license.
```
