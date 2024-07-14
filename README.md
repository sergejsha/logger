[![Maven Central](http://img.shields.io/maven-central/v/de.halfbit/logger.svg)](https://central.sonatype.com/artifact/de.halfbit/logger)
![maintenance-status](https://img.shields.io/badge/maintenance-actively--developed-brightgreen.svg)

# 🪵 Logger

Minimalistic Logger for Kotlin Multiplatform from one of my projects,
shared with the awesome Kotlin community.

![Architecture diagram](http://www.plantuml.com/plantuml/proxy?cache=no&src=https://raw.githubusercontent.com/sergejsha/logger/master/documentation/architecture.iuml)

# Usage

```kotlin
// Initialize
class App : Application() {
    override fun onCreate() {
        initializeLogger {
            registerAndroidLogSink()
            registerPrintlnSink() // default, if no sinks are configured
            registerMemoryRingSink()
            loggableLevel = if (isDebugBuild) Everything else InfoAndSevere
        }
    }
}

// Use logger
private const val TAG = "Reader"

class Reader {
    init {
        i(TAG) { "created: size=${bytes?.size}" } // info
        d(TAG) { "created: size=${bytes?.size}" } // debug
    }

    suspend fun readData() {
        try {
            httpClient.post("read-data")
        } catch (e: Exception) {
            w(TAG, e) { "read-data failed" } // warning
            e(TAG, e) { "read-data failed" } // error
        }
    }
}
```

# Dependencies

In `gradle/libs.versions.toml`

```toml
[versions]
kotlin = "2.0.0"
logger = "0.3"

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

# Release

1. Bump version in `root.publication.gradle.kts` of the root project
2. `./gradlew clean build publishAllPublicationsToCentralRepository`

# Release Notes

- 0.3 MemoryRingSink prints log entries lazily.
- 0.2 Improve log layouts for Println and MemoryRing sinks.
- 0.1 Initial release

# License

```
Copyright 2024 Sergej Shafarenka, www.halfbit.de

You are FREE to use, copy, redistribute, remix, transform, and build 
upon the material or its derivative FOR ANY LEGAL PURPOSES.

Any distributed derivative work containing this material or parts 
of it must have this copyright attribution notices.

The material is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES 
OR CONDITIONS OF ANY KIND, either express or implied.
```
