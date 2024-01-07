
plugins {
    id("java-library")
}

sourceSets {
    main {
        output.setResourcesDir(file("build/classes/java/main"))
    }
}

group = "jwabbit"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
}

tasks.compileJava {
    options.javaModuleVersion = provider { "1.0" }
}
