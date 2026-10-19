package com.github.ahatem.qtranslate.plugins.lens

import com.github.ahatem.qtranslate.api.plugin.PluginSettings
import com.github.ahatem.qtranslate.api.settings.Setting
import com.github.ahatem.qtranslate.api.settings.SettingType

/**
 * Optional override for the bundled Google Lens key.
 *
 * Blank means "use the bundled default" — the bundled key is never copied into
 * the user's settings store and never appears as this field's default value.
 */
data class GoogleLensSettings(
    @field:Setting(
        label = "API key override",
        description = "Optional. Leave blank to use the bundled Google Lens key.",
        type = SettingType.PASSWORD,
        order = 10
    )
    var apiKey: String = ""
) : PluginSettings.Configurable()
