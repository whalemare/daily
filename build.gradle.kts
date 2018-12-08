import com.github.rholder.gradle.task.OneJar
import org.gradle.internal.impldep.org.apache.tools.zip.JarMarker
import org.gradle.jvm.tasks.Jar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import sun.tools.jar.resources.jar

buildscript {
    var kotlinVersion: String by extra
    kotlinVersion = "1.3.11"

    repositories {
        mavenCentral()
    }
    dependencies {
        classpath(kotlinModule("gradle-plugin", kotlinVersion))
        classpath("com.github.rholder:gradle-one-jar:1.0.4")
    }
}

group = "ru.whalemare"
version = "1.0-SNAPSHOT"

apply {
    plugin("kotlin")
    plugin("gradle-one-jar")
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
    implementation("org.eclipse.jgit:org.eclipse.jgit:4.7.0.201704051617-r")

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

tasks.withType<Jar> {
    baseName = "ash"
    manifest {
        attributes(mapOf("Main-Class" to "Main"))
    }
}

tasks.withType<OneJar> {
    mainClass = "Main"
    archiveName = "ash.jar"
}

//jar {
//    baseName = "mira"
//    manifest {
//        attributes 'Main-Class': 'Main'
//    }
//}
//
//task makeJar(type: OneJar) {
//    mainClass = 'Main'
//    archiveName = 'mira.jar'
//}