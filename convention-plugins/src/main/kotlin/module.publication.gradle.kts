plugins {
    id("maven-publish")
    id("signing")
}

publishing {
    publications.withType<MavenPublication> {
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
                    name.set("Custom License")
                    url.set("https://github.com/sergejsha/logger")
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
                url.set("https://github.com/sergejsha/logger")
            }
        }
    }
}

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
//          https://youtrack.jetbrains.com/issue/KT-46466 is fixed
tasks.withType<AbstractPublishToMaven>().configureEach {
    dependsOn(project.tasks.withType(Sign::class.java))
}

