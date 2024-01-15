import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = "com.camp.wms"
version = "2.0"

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
            packageName = "camp-wms"
            packageVersion = "2.0.0"
            windows{
                iconFile.set(project.file("innox_logo.ico"))
            }
            linux{
                iconFile.set(project.file("innox_llogo.png"))
            }
            modules("java.compiler", "java.instrument" , "java.sql", "jdk.unsupported", "java.naming")
        }
    }
}

dependencies {
    commonMainImplementation("org.jetbrains.compose.material:material-icons-extended-desktop:1.1.0")
    commonMainImplementation("mysql:mysql-connector-java:8.0.29")
}

