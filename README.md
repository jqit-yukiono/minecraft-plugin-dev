# minecraft-plugin-dev

mc01(本番、Paper 26.1.2)向けプラグインのローカル開発環境。

## 構成

- 言語: Kotlin(JVM 25 ターゲット)
- 対象API: `io.papermc.paper:paper-api` — mc01 実機のピン版(26.1.2)に合わせている
- Kotlin stdlib: 同梱しない(`compileOnly`)。mc01 には
  [KotlinPlugins](https://modrinth.com/plugin/kotlinplugins) が導入済みで、
  実行時に提供される前提([[NightVision]]と同じ作り方)。ローカルのテストサーバにも
  同プラグインを入れておくこと。
- `src/main/kotlin/info/sk4869/minecraft/ExamplePlugin.kt` — 動作確認用の最小プラグイン。
  実際に作るプラグインに合わせてリネーム/置き換える(`gradle.properties` の
  `plugin_main` も合わせて変更)。

## 必要な環境

- JDK 25(`jenv local 25.0.4.1` 済み、`.java-version` で固定)
- Gradle本体は不要(`./gradlew` が自動取得する)

JDKの実パスが Gradle の toolchain 自動検出に乗らない場合(Homebrew の keg-only 版等)、
`gradle.properties` の `org.gradle.java.installations.paths` を実際のパスに合わせる。

## よく使うコマンド

```bash
./gradlew build              # ビルド(build/libs/ にjarができる)
./gradlew deployToTestServer # build/libs/ の jar を test-server/plugins/ へコピー
```

## ローカルテストサーバ(Docker)

```bash
./gradlew deployToTestServer     # build/libs/ の jar を test-server/plugins/ へ配置
cd test-server && docker compose up -d
docker compose logs -f           # 起動確認(KotlinPlugins → ExamplePlugin の順でロードされる)
```

- 接続先: `localhost:25566`(本番mc01の25565とは別ポート)。`ONLINE_MODE=FALSE`なので
  Mojang認証不要、どのユーザー名でもそのまま接続できる
- `KotlinPlugins`は本番と同じ経路(`MODRINTH_PROJECTS`)で自動DLされる
- プラグイン更新後は `./gradlew deployToTestServer && cd test-server && docker compose restart`
- IntelliJ からのリモートデバッグ: Run > Edit Configurations > Remote JVM Debug で
  `localhost:5005` にアタッチ(`JVM_OPTS`でJDWP有効化済み、`suspend=n`なので
  デバッガ未接続でも起動は進む)
- データ(`test-server/data/`)はgitignore対象。クリーンな状態からやり直す場合は
  `docker compose down && rm -rf data` でリセット

## paper-api のバージョン表記について

26.x系からは `<version>.build.<N>-stable`(または `-alpha`)という形式に変わっている
(1.21.11 までの `-R0.1-SNAPSHOT` とは別物)。最新のビルド番号は
[maven-metadata.xml](https://repo.papermc.io/repository/maven-public/io/papermc/paper/paper-api/maven-metadata.xml)
で確認する。
