package com.github.wakingrufus.eloleague.swiss

import com.github.wakingrufus.eloleague.player.PlayerItem
import javafx.beans.property.SimpleListProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import tornadofx.*
import java.util.*

class SwissTeamItem(id: String = UUID.randomUUID().toString(),
                    name: String = "") {
    val idProperty = SimpleStringProperty(this, "id", id)
    var id by idProperty

    val nameProperty = SimpleStringProperty(this, "name", name)
    var name by nameProperty

    val playersProperty = SimpleListProperty<PlayerItem>(
            this,
            "players",
            FXCollections.observableArrayList())
    var players by playersProperty
}