import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("net.neoforged.moddev") version "2.0.147"
}

version = "${property("mod.version")}+${sc.current.version}"
base.archivesName.set("${property("mod.id") as String}-neoforge")

val requiredJava = when {
    sc.current.parsed >= "26.1" -> JavaVersion.VERSION_25
    sc.current.parsed >= "1.20.5" -> JavaVersion.VERSION_21
    sc.current.parsed >= "1.18" -> JavaVersion.VERSION_17
    else -> JavaVersion.VERSION_16
}

sourceSets {
    create("client") {
        compileClasspath += sourceSets.main.get().output
        runtimeClasspath += sourceSets.main.get().output
    }
}

configurations {
    named("clientImplementation") {
        extendsFrom(configurations["implementation"])
    }
    named("clientCompileClasspath") {
        extendsFrom(configurations["compileClasspath"])
    }
    named("clientRuntimeClasspath") {
        extendsFrom(configurations["runtimeClasspath"])
    }
}

repositories {
    mavenCentral()
    maven {
        name = "NeoForged"
        url = uri("https://maven.neoforged.net/releases")
    }
    maven {
        name = "Modrinth"
        url = uri("https://api.modrinth.com/maven")
    }
    maven("https://maven.midnightdust.eu/releases")
    maven("https://thedarkcolour.github.io/KotlinForForge/")
}

dependencies {
    implementation("thedarkcolour:kotlinforforge-neoforge:${sc.properties.get<String>("deps.kff")}") {
        exclude(group = "thedarkcolour", module = "kffmod-neoforge")
    }

    val midnightlib = "eu.midnightdust:midnightlib:${sc.properties.get<String>("deps.midnightlib")}"
    implementation(midnightlib)
    jarJar(midnightlib)

    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
}

tasks.test {
    useJUnitPlatform()
}

neoForge {
    version = property("deps.neo_loader") as String

    mods {
        register("quickglance") {
            sourceSet(sourceSets.main.get())
            sourceSet(sourceSets.getByName("client"))
        }
    }

    runs {
        register("client") {
            gameDirectory = rootProject.file("run")
            client()
        }
        register("server") {
            gameDirectory = rootProject.file("run")
            server()
        }
    }
}

java {
    withSourcesJar()
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(requiredJava.majorVersion.toInt()))
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

tasks.processResources {
    val props = mapOf(
        "id" to sc.properties.get<String>("mod.id"),
        "name" to sc.properties.get<String>("mod.name"),
        "version" to project.version.toString(),
        "minecraft" to sc.properties.get<String>("mod.mc_compat")
    )

    filesMatching("META-INF/neoforge.mods.toml") {
        expand(props)
    }

    exclude("fabric.mod.json")
}

tasks.named<Jar>("jar") {
    from(sourceSets["client"].output)
}

tasks.register<Copy>("buildAndCollect") {
    group = "build"
    description = "Builds mod jar and copies it to build/libs"
    from(tasks.named("jar"))
    into(rootProject.layout.buildDirectory.file("libs"))
}
