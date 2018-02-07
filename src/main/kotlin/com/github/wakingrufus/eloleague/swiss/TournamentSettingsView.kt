package com.github.wakingrufus.eloleague.swiss

import com.github.wakingrufus.eloleague.league.LeagueItem
import com.github.wakingrufus.eloleague.league.LeagueModel
import mu.KLogging
import tornadofx.*

class TournamentSettingsView : View("Edit Tournament") {
    companion object : KLogging()

    val league: LeagueItem by param()
    val tournament: SwissTournamentModel by param()

    override val root = fieldset("Tournament Settings") {
        style {
            paddingAll = 6.px.value
        }
        field("Name") {
            textfield(tournament.name)
        }


    }



}
