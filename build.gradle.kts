import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = "com.robomaster.goods"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }
        val jvmTest by getting
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "疆来计划 仓库管理系统"
            packageVersion = "1.1.1"
            windows{
                iconFile.set(project.file("robomaster_logo_black.ico"))
            }
            linux{
                iconFile.set(project.file("robomaster_logo_black.png"))
            }
            modules("java.compiler", "java.instrument" , "java.sql", "jdk.unsupported", "java.naming")
        }
    }
}

dependencies {
    commonMainImplementation("org.jetbrains.compose.material:material-icons-extended-desktop:1.1.0")
    commonMainImplementation("mysql:mysql-connector-java:8.0.29")
}

