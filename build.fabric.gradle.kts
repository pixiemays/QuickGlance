import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("dev.kikugie.loom-back-compat")
    id("maven-publish")
}

version = "${property("mod.version")}+${sc.current.version}"
base.archivesName.set("${property("mod.id") as String}-fabric")

val requiredJava: JavaVersion = when {
    sc.current.parsed >= "26.1" -> JavaVersion.VERSION_25
    sc.current.parsed >= "1.20.5" -> JavaVersion.VERSION_21
    sc.current.parsed >= "1.18" -> JavaVersion.VERSION_17
    else -> JavaVersion.VERSION_16
}

repositories {
    mavenCentral()
    maven {
        name = "Modrinth"
        url = uri("https://api.modrinth.com/maven")
    }
    maven("https://maven.midnightdust.eu/releases")
    maven("https://maven.terraformersmc.com/releases/") {
        name = "Terraformers"
    }
}

dependencies {
    minecraft("com.mojang:minecraft:${sc.current.version}")
    loomx.applyMojangMappings()

    modImplementation("net.fabricmc:fabric-loader:${property("deps.fabric_loader")}")
    modImplementation("net.fabricmc:fabric-language-kotlin:${sc.properties.get<String>("deps.fabric_kotlin")}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${sc.properties.get<String>("deps.fabric_api")}")

    val midnightlib = "eu.midnightdust:midnightlib:${sc.properties.get<String>("deps.midnightlib")}"
    modImplementation(midnightlib)
    include(midnightlib)

    // ModMenu for client config screen
    modCompileOnly("maven.modrinth:mOgUt4GM:A1KYIXpK")

    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
}

tasks.test {
    useJUnitPlatform()
}

loom {
    splitEnvironmentSourceSets()

    mods {
        register("quickglance") {
            sourceSet("main")
            sourceSet("client")
        }
    }

    runConfigs.all {
        preferGradleTask = true
        generateRunConfig = true
        runDirectory = rootProject.file("run")
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
        "version" to project.version.toString(),
        "loader_version" to sc.properties.get<String>("deps.fabric_loader"),
        "kotlin_loader_version" to sc.properties.get<String>("deps.fabric_kotlin"),
        "minecraft" to sc.properties.get<String>("mod.mc_compat")
    )

    filesMatching("fabric.mod.json") {
        expand(props)
    }

    exclude("META-INF/neoforge.mods.toml")
}

tasks.register<Copy>("buildAndCollect") {
    group = "build"
    description = "Builds mod jar and copies it to build/libs"
    from(loomx.modJar.flatMap { it.archiveFile })
    into(rootProject.layout.buildDirectory.file("libs"))
}
