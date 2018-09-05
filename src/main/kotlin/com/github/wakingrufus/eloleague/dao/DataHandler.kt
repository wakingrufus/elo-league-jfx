package com.github.wakingrufus.eloleague.dao

import com.github.wakingrufus.eloleague.data.ConfigData


interface DataHandler {

    fun readFileConfig(): ConfigData
    fun saveConfig(configData: ConfigData)
}