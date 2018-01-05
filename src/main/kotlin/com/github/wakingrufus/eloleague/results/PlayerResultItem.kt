package com.github.wakingrufus.eloleague.results

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty

class PlayerResultItem(id: String,
                       name: String = "",
                       currentRating: Int,
                       gamesPlayed: Int,
                       wins: Int,
                       losses: Int) {
    val idProperty = SimpleStringProperty(this, "id", id)
    val nameProperty = SimpleStringProperty(this, "name", name)
    val currentRatingProperty = SimpleIntegerProperty(this, "currentRating", currentRating)
    val gamesPlayedProperty = SimpleIntegerProperty(this, "gamesPlayed", gamesPlayed)
    val winsProperty = SimpleIntegerProperty(this, "wins", wins)
    val lossesProperty = SimpleIntegerProperty(this, "losses", losses)
}
