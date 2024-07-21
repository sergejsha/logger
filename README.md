[![Maven Central](http://img.shields.io/maven-central/v/de.halfbit/logger.svg)](https://central.sonatype.com/artifact/de.halfbit/logger)
![maintenance-status](https://img.shields.io/badge/maintenance-actively--developed-brightgreen.svg)

# 🪵 Logger

Minimalistic and concise Logger for Kotlin Multiplatform from one of my projects,
shared with the awesome Kotlin community.

![Architecture diagram](http://www.plantuml.com/plantuml/proxy?src=https://raw.githubusercontent.com/sergejsha/logger/master/documentation/architecture.v3.iuml)

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
logger = "0.5"

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

# Releasing

1. Bump version in `root.publication.gradle.kts`
2. `./gradlew clean build publishAllPublicationsToCentralRepository`

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
