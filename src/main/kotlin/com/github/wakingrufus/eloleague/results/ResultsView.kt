package com.github.wakingrufus.eloleague.results

import com.github.wakingrufus.elo.LeagueState
import com.github.wakingrufus.eloleague.data.PlayerData
import com.github.wakingrufus.eloleague.player.PlayerItem
import mu.KLogging
import tornadofx.*

class ResultsView : View() {
    companion object : KLogging()

    val players : List<PlayerItem> by param()
    val leagueState : LeagueState by param()

    override val root = vbox {
        id = "results-wrapper"

    }


}