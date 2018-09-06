package com.github.wakingrufus.eloleague.dao

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule
import com.github.wakingrufus.eloleague.data.ConfigData
import mu.KLogging
import java.io.File
import java.io.IOException

class JacksonFileDataHandler(val file: File = File(File(System.getProperty("user.home")), ".elo.json"))
    : DataHandler {
    companion object : KLogging() {
        val objectMapper = ObjectMapper()
                .registerModule(ParameterNamesModule())
                .registerModule(KotlinModule())
    }

    override fun readFileConfig(): ConfigData {
        return if (!file.exists()) {
            logger.info("data not found, starting new data object")
            ConfigData()
        } else {
            try {
                objectMapper.readValue(file, ConfigData::class.java)
            } catch (e: IOException) {
                logger.error("Error reading data file: " + e.localizedMessage, e)
                ConfigData()
            }
        }
    }

    override fun saveConfig(configData: ConfigData) {
        if (!file.exists()) {
            logger.info("data file not found, creating")
            try {
                file.createNewFile()
            } catch (e: IOException) {
                logger.error("Error creating data file: " + e.localizedMessage, e)
            }
        }
        objectMapper.writeValue(file, configData)
    }

}