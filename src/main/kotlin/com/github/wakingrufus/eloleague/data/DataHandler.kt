package com.github.wakingrufus.eloleague.data


interface DataHandler {

    fun readFileConfig(): ConfigData
    fun saveConfig(configData: ConfigData)
}