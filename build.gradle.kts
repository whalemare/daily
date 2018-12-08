import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    var kotlinVersion: String by extra
    kotlinVersion = "1.3.11"

    repositories {
        mavenCentral()
    }
    dependencies {
        classpath(kotlinModule("gradle-plugin", kotlinVersion))
    }
}

group = "ru.whalemare"
version = "1.0-SNAPSHOT"

apply {
    plugin("kotlin")
}

val kotlinVersion: String by extra

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlinModule("stdlib-jdk8", kotlinVersion))
    implementation("info.picocli:picocli:3.8.2")
    implementation("org.zeroturnaround:zt-exec:1.10")
    implementation("org.slf4j:slf4j-nop:1.7.21")

    testCompile("org.junit.jupiter:junit-jupiter-api:5.3.2")
    testCompile("org.junit.jupiter:junit-jupiter-params:5.3.2")
    testRuntime("org.junit.jupiter:junit-jupiter-engine:5.3.2")
}

tasks.withType<Test> {
    testLogging {
        events("passed", "skipped", "failed")
    }
}

tasks.withType<Wrapper> {
    gradleVersion = "4.8"
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}