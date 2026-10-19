package com.github.ahatem.qtranslate.plugins.lens

import com.github.ahatem.qtranslate.api.plugin.Plugin
import com.github.ahatem.qtranslate.api.plugin.PluginContext
import com.github.ahatem.qtranslate.api.plugin.Service
import com.github.ahatem.qtranslate.api.plugin.ServiceError
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result

class GoogleLensPlugin : Plugin<GoogleLensSettings> {

    private lateinit var pluginContext: PluginContext
    private var settings = GoogleLensSettings()
    private var activeServices: List<Service> = emptyList()

    override suspend fun initialize(context: PluginContext): Result<Unit, ServiceError> {
        this.pluginContext = context
        settings = GoogleLensSettings(
            apiKey = context.secrets.get(KEY_API_KEY).orEmpty()
        )
        pluginContext.logger.info("Google Lens Plugin initialized")
        return Ok(Unit)
    }

    override suspend fun onEnable(): Result<Unit, ServiceError> {
        pluginContext.logger.info("Enabling Google Lens OCR service")
        activeServices = listOf(
            GoogleLensOCRService(
                pluginContext = pluginContext,
                httpClient = pluginContext.http,
                settings = { settings }
            )
        )
        return Ok(Unit)
    }

    override suspend fun onDisable() {
        pluginContext.logger.info("Disabling Google Lens OCR service")
        activeServices = emptyList()
    }

    override suspend fun shutdown() {
        pluginContext.logger.info("Google Lens Plugin shutting down")
    }

    override fun getServices(): List<Service> = activeServices

    override fun getSettings(): GoogleLensSettings = settings

    override suspend fun onSettingsChanged(settings: GoogleLensSettings): Result<Unit, ServiceError> {
        val apiKey = settings.apiKey.trim()
        if (apiKey.isBlank()) {
            pluginContext.secrets.remove(KEY_API_KEY)
        } else {
            pluginContext.secrets.put(KEY_API_KEY, apiKey)
        }
        this.settings = settings.copy(apiKey = apiKey)
        return Ok(Unit)
    }

    private companion object {
        const val KEY_API_KEY = "apiKey"
    }
}
