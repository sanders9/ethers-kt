plugins {
    `project-conventions`
    `maven-publish-conventions`
}

kotlin {
    sourceSets {
        val jvmSharedTest by getting {
            dependencies {
                implementation(libs.bundles.kotest)
            }
        }
    }
}
