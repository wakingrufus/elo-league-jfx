package com.github.wakingrufus.eloleague

import com.github.wakingrufus.eloleague.data.ConfigData
import com.github.wakingrufus.eloleague.data.DataHandler
import com.github.wakingrufus.eloleague.data.FileDataHandler
import com.github.wakingrufus.eloleague.league.LeagueItem
import com.github.wakingrufus.eloleague.league.fromData
import com.github.wakingrufus.eloleague.league.toData
import javafx.collections.FXCollections
import java.io.File

object State {
    val configHandler: DataHandler =
            FileDataHandler(File(File(System.getProperty("user.home")), ".elo.json"))

    val localLeagues = FXCollections.observableArrayList<LeagueItem>()

    fun loadFromFile() {
        val configData = configHandler.readFileConfig()
        if (configData.leagues.isNotEmpty()) {
            localLeagues.setAll(configData.leagues.map { fromData(it) })
        }
    }

    val remoteLeagues = FXCollections.observableHashMap<String,LeagueItem>()

    fun updateRemoteLeague(leagueItem : LeagueItem){
        remoteLeagues.put(leagueItem.id,leagueItem)
    }

    fun saveToFile() {
        configHandler.saveConfig(
                ConfigData(leagues = localLeagues
                        .filterNotNull()
                        .map { toData(it) }
                        .toSet()))
    }

    fun addNewLeague(league: LeagueItem){
        localLeagues.add(league)
    }

    fun removeLeague(league: LeagueItem){
        localLeagues.remove(league)
    }
}