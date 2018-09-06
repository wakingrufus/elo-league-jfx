package com.github.wakingrufus.eloleague.dao

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule
import com.github.wakingrufus.eloleague.data.ConfigData
import mu.KLogging
import java.io.IOException
import java.net.URL

class JacksonUrlDataHandler(val url: URL) : DataHandler {
    companion object : KLogging() {
        val objectMapper = ObjectMapper()
                .registerModule(ParameterNamesModule())
                .registerModule(KotlinModule())
    }

    override fun readFileConfig(): ConfigData {
        return try {
            objectMapper.readValue(url, ConfigData::class.java)
        } catch (e: IOException) {
            logger.error("Error reading data file: " + e.localizedMessage, e)
            ConfigData()
        }
    }

    override fun saveConfig(configData: ConfigData) {
        System.out.println("save url not implemented")
    }

}