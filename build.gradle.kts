buildscript {
    repositories {
        gradlePluginPortal()
    }
}

plugins {
    java
    checkstyle
}

project.extra["GithubUrl"] = "https://github.com/OreoCupcakes/kotori-plugins-releases"

apply<BootstrapPlugin>()
apply<VersionPlugin>()

allprojects {
    group = "com.theplug.kotori"
    apply<MavenPublishPlugin>()
    version = ProjectVersions.runeliteVersion
}

tasks.named("bootstrapPlugins") {
    finalizedBy("copyBootstrap")
}

tasks.register<Copy>("copyBootstrap") {
    println("Copying bootstrap to main dir.")
    from("./build/bootstrap/")
    into(System.getProperty("user.home") + "/Documents/RuneLitePlugins/Jars")
    eachFile {
        if (this.relativePath.getFile(destinationDir).exists() && this.sourceName != "plugins.json") {
            this.exclude()
            println("Excluding " + this.sourceName + " as its the same version.")
        }
    }
}

subprojects {
    group = "com.theplug.kotori"

    project.extra["PluginProvider"] = "Kotori"
    project.extra["ProjectSupportUrl"] = "https://discord.gg/JZFhwNVH85"
    project.extra["PluginLicense"] = "2-Clause BSD License"

    repositories {
        maven {
            url = uri("https://repo.runelite.net")
        }
        mavenCentral()
        mavenLocal()

        exclusiveContent {
            forRepository {
                maven {
                    url = uri("https://repo.runelite.net")
                }
            }
            filter {
                includeModule("net.runelite", "discord")
            }
        }
    }

    apply<JavaPlugin>()

    val version = "latest.release"
//    val version = "1.10.36"

    dependencies {
        annotationProcessor(Libraries.lombok)
        annotationProcessor(Libraries.pf4j)

        compileOnly(group = "net.runelite", name = "client", version = version)
        compileOnly(group = "net.runelite", name = "runelite-api", version = version)

        compileOnly(Libraries.findbugs)
        compileOnly(Libraries.apacheCommonsText)
        compileOnly(Libraries.gson)
        compileOnly(Libraries.guice)
        compileOnly(Libraries.lombok)
        compileOnly(Libraries.okhttp3)
        compileOnly(Libraries.pf4j)
        compileOnly(Libraries.rxjava)

        testImplementation("junit:junit:4.12")
        testImplementation(group = "net.runelite", name = "client", version = version)
        testImplementation(group = "net.runelite", name = "jshell", version = version)
    }

    configure<PublishingExtension> {
        repositories {
            maven {
                url = uri("${layout.buildDirectory}/repo")
            }
        }
        publications {
            register("mavenJava", MavenPublication::class) {
                from(components["java"])
            }
        }
    }

    configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    tasks {
        withType<JavaCompile> {
            options.encoding = "UTF-8"
        }

        withType<Jar> {
            doLast {
                copy {
                    from("./build/libs/")
                    into("../release/")
                }
            }
        }

        withType<AbstractArchiveTask> {
            isPreserveFileTimestamps = false
            isReproducibleFileOrder = true
        }

        register<Copy>("copyDeps") {
            into("./build/deps/")
            from(configurations["runtimeClasspath"])
        }
    }
}