plugins {
    // Apply the java plugin to add support for Java
    java

    // Apply the application plugin to add support for building a CLI application
    // You can run your app via task "run": ./gradlew run
    application

    /*
    * Adds tasks to export a runnable jar.
    * In order to create it, launch the "shadowJar" task.
    * The runnable jar will be found in build/libs/projectname-all.jar
    */
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

repositories {
    mavenCentral()
}

val javaFXModules = listOf(
    "base",
    "controls",
    "fxml",
    "swing",
    "graphics"
)

val supportedPlatforms = listOf("linux", "mac", "win") // All required for OOP

dependencies {
    // Suppressions for SpotBugs
    compileOnly("com.github.spotbugs:spotbugs-annotations:4.8.6")

    val javaFxVersion = "23.0.1"
    for (platform in supportedPlatforms) {
        for (module in javaFXModules) {
            implementation("org.openjfx:javafx-$module:$javaFxVersion:$platform")
        }
    }

    val jUnitVersion = "5.11.3"
    // JUnit API and testing engine
    testImplementation("org.junit.jupiter:junit-jupiter-api:$jUnitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$jUnitVersion")

    val mockitoVersion = "5.12.0"
    // Mockito core
    testImplementation("org.mockito:mockito-core:$mockitoVersion")

    // Bouncy castle
    implementation("org.bouncycastle:bcprov-jdk18on:1.80")

    // Jackson to read json file
    // https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-core
    implementation("com.fasterxml.jackson.core:jackson-core:2.18.2")
    // https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-databind
    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.2")

    // Apache Commons Codec
    implementation("commons-codec:commons-codec:1.15") 

    // TOTP library
    // https://mvnrepository.com/artifact/com.github.bastiaanjansen/otp-java
    implementation("com.github.bastiaanjansen:otp-java:2.1.0")

    // QR CODE
    // https://mvnrepository.com/artifact/com.google.zxing/core
    implementation("com.google.zxing:core:3.5.3")
    // https://mvnrepository.com/artifact/com.google.zxing/javase
    implementation("com.google.zxing:javase:3.5.3")
}

tasks.withType<Test> {
    // Enables JUnit 5 Jupiter module
    useJUnitPlatform()
}

application {
    mainClass.set("com.zysn.passwordmanager.main.App")
}
