pluginManagement {
    repositories {
        // SCIJava Maven Repository
        maven { setUrl("https://maven.scijava.org/content/repositories/public/") }

        // Aliyun Maven Repository
//        maven { setUrl("https://maven.aliyun.com/repository/public/") }
//        maven { setUrl("https://maven.aliyun.com/repository/google/") }
//        maven { setUrl("https://maven.aliyun.com/repository/gradle-plugin/") }

        // Huawei Maven Repository
//        maven { setUrl("https://repo.huaweicloud.com/repository/maven/") }

        // Jitpack Repository
        maven { setUrl("https://jitpack.io") }

        mavenLocal()
        mavenCentral()
        google()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        // SCIJava Maven Repository
        maven { setUrl("https://maven.scijava.org/content/repositories/public/") }

        // Aliyun Maven Repository
//        maven { setUrl("https://maven.aliyun.com/repository/public/") }
//        maven { setUrl("https://maven.aliyun.com/repository/gradle-plugin/") }

        // Huawei Maven Repository
//        maven { setUrl("https://repo.huaweicloud.com/repository/maven/") }

        // Jitpack Repository
        maven { setUrl("https://jitpack.io") }

        mavenLocal()
        mavenCentral()
        google()
    }
}

rootProject.name = "InstallerX"
include(
    ":app",
    ":hidden-api"
)
