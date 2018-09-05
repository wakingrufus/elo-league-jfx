package com.github.wakingrufus.eloleague.league

import javafx.collections.FXCollections
import tornadofx.*
import java.util.*

class LeagueListController : Controller() {

    val leagues = FXCollections.observableArrayList<LeagueItem>(
            LeagueItem(id = UUID.randomUUID().toString())
    )

    fun newLeague(): LeagueItem {
        return LeagueItem(id = UUID.randomUUID().toString())
    }
}