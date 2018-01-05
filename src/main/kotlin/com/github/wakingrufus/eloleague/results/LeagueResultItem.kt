package com.github.wakingrufus.eloleague.results

import javafx.beans.property.SimpleListProperty
import javafx.collections.FXCollections
import tornadofx.getValue
import tornadofx.setValue

class LeagueResultItem {
    val playersProperty = SimpleListProperty<PlayerResultItem>(this, "players", FXCollections.observableArrayList())
    var players by playersProperty

    val gamesProperty = SimpleListProperty<GameResultItem>(this, "games", FXCollections.observableArrayList())
   var games by gamesProperty
}