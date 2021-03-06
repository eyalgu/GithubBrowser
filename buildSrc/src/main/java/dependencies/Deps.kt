package dependencies
object Deps {
    object Kotlin {
        private val version = "1.3.61"

        val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"
        val std = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$version"
    }

    object Gradle {
        val plugin = "com.android.tools.build:gradle:3.6.0-rc01"
    }

    object AndroidX {
        val appCompat = "androidx.appcompat:appcompat:1.1.0"
        val core =  "androidx.core:core-ktx:1.1.0"
        val constraintLayout =  "androidx.constraintlayout:constraintlayout:1.1.3"

        object Test {
            val junitExt = "androidx.test.ext:junit:1.1.1"
            val espressoCore =  "androidx.test.espresso:espresso-core:3.2.0"
        }
        object Fragment {
            private val version = "1.2.0"
            val fragmentKtx = "androidx.fragment:fragment-ktx:$version"
            val fragmentTesting = "androidx.fragment:fragment-testing:$version"
        }
    }

    object Junit {
        val junit = "junit:junit:4.13"
    }

}