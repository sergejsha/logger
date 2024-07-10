plugins {
    id("maven-publish")
}

allprojects {
    group = "de.halfbit"
    version = "0.1-SNAPSHOT"
}

publishing {
    repositories {
        maven {
            name = "local"
            url = uri("${layout.buildDirectory}/repository")
        }
        maven {
            name = "central"
            url = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = project.getPropertyOrEmptyString("publishing.nexus.user")
                password = project.getPropertyOrEmptyString("publishing.nexus.password")
            }
        }
        maven {
            name = "snapshot"
            url = uri("https://oss.sonatype.org/content/repositories/snapshots")
            credentials {
                username = project.getPropertyOrEmptyString("publishing.nexus.user")
                password = project.getPropertyOrEmptyString("publishing.nexus.password")
            }
        }
    }
}

private fun Project.getPropertyOrEmptyString(name: String): String =
    if (hasProperty(name)) property(name) as String? ?: "" else ""
