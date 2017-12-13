package com.github.wakingrufus.eloleague.player

import com.github.wakingrufus.eloleague.data.PlayerData
import javafx.beans.property.SimpleStringProperty
import tornadofx.getValue
import tornadofx.setValue

class PlayerItem(id: String,
                 name: String = "") {
    val idProperty = SimpleStringProperty(this, "id", id)
    var id by idProperty

    val nameProperty = SimpleStringProperty(this, "name", name)
    var name by nameProperty

}


fun toData(item: PlayerItem): PlayerData {
    return PlayerData(
            id = item.id,
            name = item.name
    )
}

fun fromData(data: PlayerData): PlayerItem {
    return PlayerItem(
            id = data.id,
            name = data.name
    )
}
