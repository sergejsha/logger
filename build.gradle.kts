import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.Base64

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlinx.atomicfu)
    alias(libs.plugins.dokka)
    id("signing")
    id("maven-publish")
}

group = "de.halfbit"
version = "0.9"

repositories {
    google()
    mavenCentral()
}

kotlin {
    explicitApi()

    jvm {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_1_8)
        }
    }

    androidTarget {
        publishLibraryVariants("release")
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_1_8)
        }
    }

    linuxX64()
    mingwX64()
    macosX64()
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser {
            commonWebpackConfig {
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(project.projectDir.path)
                    }
                }
            }
            testTask {
                useKarma {
                    useFirefoxHeadless()
                }
            }
        }
    }
    js {
        browser {
            testTask {
                useKarma {
                    useFirefoxHeadless()
                }
            }
        }
        nodejs()
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    applyDefaultHierarchyTemplate {
        common {
            group("java") {
                withJvm()
                withAndroidNative()
            }
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.atomicfu)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        all {
            languageSettings {
                optIn("kotlin.time.ExperimentalTime")
            }
        }
    }
}

android {
    namespace = "de.halfbit.logger"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    testOptions {
        unitTests {
            isReturnDefaultValues = true
        }
    }
}

val canSignArtifacts = project.hasProperty("signing.keyId")
if (canSignArtifacts) {

    val javadocJar by tasks.registering(Jar::class) {
        archiveClassifier.set("javadoc")
        dependsOn(tasks.named("dokkaGeneratePublicationHtml"))
        from(layout.buildDirectory.dir("dokka/html"))
    }

    publishing {
        repositories {
            maven {
                name = "local"
                url = uri("${layout.buildDirectory}/repository")
            }
            maven {
                name = "central"
                url =
                    uri("https://ossrh-staging-api.central.sonatype.com/service/local/staging/deploy/maven2/")
                credentials {
                    username = project.getPropertyOrEmpty("publishing.nexus.user")
                    password = project.getPropertyOrEmpty("publishing.nexus.password")
                }
            }
        }

        publications.withType<MavenPublication> {
            artifact(javadocJar.get())

            pom {
                name.set(rootProject.name)
                description.set("Minimalistic, fast and configurable Logger for Kotlin Multiplatform")
                url.set("https://github.com/sergejsha/${rootProject.name}")
                licenses {
                    license {
                        name.set("Apache-2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("halfbit")
                        name.set("Sergej Shafarenka")
                        organization.set("Halfbit GmbH")
                        organizationUrl.set("http://www.halfbit.de")
                    }
                }
                scm {
                    connection.set("scm:git:git@github.com:sergejsha/${rootProject.name}.git")
                    developerConnection.set("scm:git:ssh://github.com:sergejsha/${rootProject.name}.git")
                    url.set("https://github.com/sergejsha/${rootProject.name}")
                }
            }
        }
    }

    signing {
        publishing.publications.withType<MavenPublication>().configureEach {
            sign(this)
        }
    }

    afterEvaluate {
        // fix for: https://github.com/gradle/gradle/issues/26091
        //          https://youtrack.jetbrains.com/issue/KT-46466 is fixed
        tasks.withType<AbstractPublishToMaven>().configureEach {
            dependsOn(project.tasks.withType(Sign::class.java))
        }
    }

    tasks.register("releaseToMavenCentral") {
        group = "publishing"
        description = "Publishes to staging and manually uploads to Maven Central"
        dependsOn("publishAllPublicationsToCentralRepository")

        doLast {
            val username = project.getPropertyOrEmpty("publishing.nexus.user")
            val password = project.getPropertyOrEmpty("publishing.nexus.password")
            val bearer = Base64.getEncoder().encodeToString("$username:$password".toByteArray())

            val request = HttpRequest.newBuilder()
                .uri(URI.create("https://ossrh-staging-api.central.sonatype.com/manual/upload/defaultRepository/de.halfbit"))
                .header("Authorization", "Bearer $bearer")
                .POST(HttpRequest.BodyPublishers.noBody())
                .build()

            val response = HttpClient
                .newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString())

            if (response.statusCode() != 200) {
                throw GradleException("Manual upload failed ${response.statusCode()}:[${response.body()}]")
            } else {
                println(
                    "✅ Published and uploaded to Maven Central successfully.\n" +
                            "   Release to public at https://central.sonatype.com/publishing"
                )
            }
        }
    }
}

private fun Project.getPropertyOrEmpty(name: String): String =
    if (hasProperty(name)) property(name) as String? ?: "" else ""