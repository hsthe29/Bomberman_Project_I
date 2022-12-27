import com.soywiz.korge.gradle.*


plugins {
	alias(libs.plugins.korge)
}

repositories {
    mavenCentral()
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("com.google.code.gson:gson:2.9.1")
            }
        }
    }
}
//dependencies {
//    "implementation"("com.beust:klaxon:5.5")
//}

korge {
    id = "the.thehs.thehs"
    supportExperimental3d()
    supportBox2d()
    supportDragonbones()
// To enable all targets at once

    //targetAll()

// To enable targets based on properties/environment variables
    //targetDefault()

// To selectively enable targets

    targetJvm()
    //targetJs()
    //targetDesktop()
    //targetIos()
    //targetAndroidIndirect() // targetAndroidDirect()
    bundle("https://github.com/korlibs/korge-bundles.git::korge-box2d::7439e5c7de7442f2cd33a1944846d44aea31af0a##9fd9d54abd8abc4736fd3439f0904141d9b6a26e9e2f1e1f8e2ed10c51f490fd")
}
