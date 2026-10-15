package info.sk4869.minecraft

import org.bukkit.plugin.java.JavaPlugin

/**
 * 動作確認用の最小プラグイン。実際のプラグインを作る際はこのクラスを
 * リネーム/置き換えて、plugin.yml の main も合わせて変更する。
 */
class ExamplePlugin : JavaPlugin() {
    override fun onEnable() {
        logger.info("ExamplePlugin enabled")
    }

    override fun onDisable() {
        logger.info("ExamplePlugin disabled")
    }
}
