plugins {
    alias(libs.plugins.multiplatform)
}

kotlin {
    listOf(
        linuxX64(),
    ).forEach {
        it.binaries.executable {
            entryPoint = "main"
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":shared"))
        }
    }
}
