package com.github.wakingrufus.eloleague.results

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.getValue
import tornadofx.setValue

class PlayerResultItem(id: String,
                 name: String = "",
                 currentRating: Int,
                 gamesPlayed: Int,
                 wins: Int,
                 losses: Int) {
    val idProperty = SimpleStringProperty(this, "id", id)
    var id by idProperty

    val nameProperty = SimpleStringProperty(this, "name", name)
    var name by nameProperty

    val currentRatingProperty = SimpleIntegerProperty(this, "currentRating", currentRating)
    var currentRating by currentRatingProperty

    val gamesPlayedProperty = SimpleIntegerProperty(this, "gamesPlayed", gamesPlayed)
    var gamesPlayed by gamesPlayedProperty

    val winsProperty = SimpleIntegerProperty(this, "wins", wins)
    var wins by winsProperty

    val lossesProperty = SimpleIntegerProperty(this, "losses", losses)
    var losses by lossesProperty

}
