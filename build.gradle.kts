import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.3.0"
}

group = property("group")!!
version = property("version")!!

java.toolchain {
    languageVersion.set(JavaLanguageVersion.of(25))
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    // mc01 には KotlinPlugins (https://modrinth.com/plugin/kotlinplugins) が導入済みで
    // 実行時に Kotlin stdlib を提供するため、プラグイン側では同梱しない (compileOnly)。
    compileOnly(kotlin("stdlib"))
    compileOnly("io.papermc.paper:paper-api:${property("paper_api_version")}")
}

tasks {
    withType<KotlinCompile> {
        compilerOptions.jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_25)
    }

    processResources {
        filesMatching("plugin.yml") {
            expand(project.properties)
        }
    }

    // ローカルの Docker テストサーバ(test-server/plugins)へビルド結果を配置する。
    // 絶対パスを埋め込まないよう、プロジェクト相対パスを使う。
    register<Copy>("deployToTestServer") {
        dependsOn("jar")
        from(layout.buildDirectory.dir("libs"))
        into(layout.projectDirectory.dir("test-server/plugins"))
        include("${rootProject.name}-*.jar")
    }
}
