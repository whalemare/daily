import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.internal.impldep.org.apache.tools.zip.JarMarker
import org.gradle.jvm.tasks.Jar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import sun.tools.jar.resources.jar
import org.apache.tools.ant.filters.*
import org.gradle.internal.impldep.org.apache.tools.zip.ExtraFieldUtils.register

buildscript {
    var kotlinVersion: String by extra
    kotlinVersion = "1.3.11"

    repositories {
        mavenCentral()
        maven { setUrl("https://repo.gradle.org/gradle/repo") }
    }
    dependencies {
        classpath(kotlinModule("gradle-plugin", kotlinVersion))
        classpath("com.github.jengelman.gradle.plugins:shadow:4.0.3")
    }
}

group = "ru.whalemare"
version = "1.0-SNAPSHOT"

apply {
    plugin("kotlin")
    plugin("com.github.johnrengelman.shadow")
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

tasks {
    withType<Test> {
        testLogging {
            events("passed", "skipped", "failed")
        }
    }

    withType<Wrapper> {
        gradleVersion = "4.8"
    }

    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

//    withType<Jar> {
//        baseName = "ash"
//        manifest {
//            attributes(mapOf("Main-Class" to "Main"))
//        }
//    }
}

val shadowJar: ShadowJar by tasks
shadowJar.apply {
    manifest {
        this.attributes.apply {
            put("Implementation-Title", "Gradle Jar File Example")
            put("Implementation-Version", "1")
            put("Main-Class", "Main")
        }
    }

    baseName = "ash"
    archiveName = "$baseName.jar"
    destinationDir = File("jar/")
}

tasks.getByName("shadowJar").finalizedBy("copyToMg")

tasks.create<Copy>("copyToMg") {
    from("jar/")
    into("/Users/whalemare/Applications/")
}