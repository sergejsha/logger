plugins {
    id("maven-publish")
    id("signing")
}

publishing {
    publications.withType<MavenPublication> {

        repositories {
            maven {
                name = "local"
                url = uri("${layout.buildDirectory}/repository")
            }
            maven {
                name = "central"
                url = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
                credentials {
                    username = project.getPropertyOrEmpty("publishing.nexus.user")
                    password = project.getPropertyOrEmpty("publishing.nexus.password")
                }
            }
            maven {
                name = "snapshot"
                url = uri("https://oss.sonatype.org/content/repositories/snapshots")
                credentials {
                    username = project.getPropertyOrEmpty("publishing.nexus.user")
                    password = project.getPropertyOrEmpty("publishing.nexus.password")
                }
            }
        }

        artifact(
            tasks.register("${name}JavadocJar", Jar::class) {
                archiveClassifier.set("javadoc")
                archiveAppendix.set(name)
            }
        )

        pom {
            name.set(this@withType.name)
            url.set("https://github.com/sergejsha/logger")

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

private fun Project.getPropertyOrEmpty(name: String): String =
    findProperty(name)?.toString() ?: ""

val canSign = project.hasProperty("signing.keyId")
if (canSign) {
    publishing {
        publications.forEach { publication ->
            signing.sign(publication)
        }
    }
    signing {
        sign(publishing.publications)
    }
}

// fix for: https://github.com/gradle/gradle/issues/26091
// and https://youtrack.jetbrains.com/issue/KT-46466
tasks.withType<AbstractPublishToMaven>().configureEach {
    dependsOn(project.tasks.withType(Sign::class.java))
}

open class LoggerPublishingPluginExtension(
    private val _description: (String) -> Unit,
) {
    fun description(value: String) {
        _description(value)
    }
}

project.extensions
    .create<LoggerPublishingPluginExtension>(
        "loggerPublishing",
        ::description,
    )

private fun description(value: String) {
    publishing {
        publications.withType<MavenPublication> {
            pom {
                description.set(value)
            }
        }
    }
}

project.afterEvaluate {
    // https://youtrack.jetbrains.com/issue/KT-61313
    tasks.withType<Sign>().configureEach {
        val publicationName = name.removePrefix("sign").removeSuffix("Publication")
        tasks.findByName("linkDebugTest$publicationName")?.let {
            mustRunAfter(it)
        }
        tasks.findByName("compileTestKotlin$publicationName")?.let {
            mustRunAfter(it)
        }
    }
}
