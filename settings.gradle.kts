pluginManagement {
    repositories {
        maven {
            url = uri("https://maven.aliyun.com/repository/central")
        }
        maven {
            url = uri("http://mirrors.cloud.tencent.com/nexus/repository/maven-public/")
            isAllowInsecureProtocol = true
        }
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
dependencyResolutionManagement {
    repositories {
        maven { url = uri("https://maven.aliyun.com/repository/google") }
        maven { url = uri("https://maven.aliyun.com/repository/jcenter") }
        maven { url = uri("https://maven.aliyun.com/repository/public") }
        maven { url = uri("https://jitpack.io") }
        google()
        mavenCentral()
    }
}

rootProject.name = "Penmedia TV"
include(":app")
include(":superplayerkit")
 