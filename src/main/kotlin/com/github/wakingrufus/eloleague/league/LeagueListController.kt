package com.github.wakingrufus.eloleague.league

import com.github.wakingrufus.elo.League
import com.github.wakingrufus.eloleague.data.ConfigData
import com.github.wakingrufus.eloleague.data.DataHandler
import com.github.wakingrufus.eloleague.data.FileDataHandler
import javafx.collections.FXCollections
import tornadofx.Controller
import java.io.File
import java.util.*

class LeagueListController : Controller() {
    private val defaultLeague: League = League(teamSize = 1)
    val leagues = FXCollections.observableArrayList<LeagueItem>(
            LeagueItem(id = UUID.randomUUID().toString())
    )
    val configHandler: DataHandler =
            FileDataHandler(File(File(System.getProperty("user.home")), ".elo.json"))

    init {
        val configData = configHandler.readFileConfig()
        if (configData.leagues.isNotEmpty()) {
            leagues.setAll(configData.leagues.map { fromData(it) })
        }
    }

    fun save() {
        log.info("saving data")
        configHandler.saveConfig(
                ConfigData(leagues = leagues
                        .filterNotNull()
                        .map { toData(it) }
                        .toSet()))
    }

    fun newLeague(): LeagueItem {
        val newLeague = LeagueItem(id = UUID.randomUUID().toString())
        newLeague.idProperty.set(UUID.randomUUID().toString())
        newLeague.kFactorBaseProperty.set(defaultLeague.kFactorBase)
        newLeague.startingRatingProperty.set(defaultLeague.startingRating)
        newLeague.teamSizeProperty.set(defaultLeague.teamSize)
        newLeague.trialKFactorMultiplierProperty.set(defaultLeague.trialKFactorMultiplier)
        newLeague.trialPeriodProperty.set(defaultLeague.trialPeriod)
        newLeague.xiProperty.set(defaultLeague.xi)
        return newLeague;
    }
}