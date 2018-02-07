package com.github.wakingrufus.eloleague.swiss

import com.github.wakingrufus.eloleague.data.SwissGameData
import javafx.beans.property.SimpleStringProperty
import java.util.*

class SwissGameItem(id: String = UUID.randomUUID().toString(),
                    gameId: String?,
                    winningTeamId: String?) {

    val idProperty = SimpleStringProperty(this, "id", id)
    val gameIdProperty = SimpleStringProperty(this, "game", gameId)
    val winningTeamIdProperty = SimpleStringProperty(this, "winningTeamId", winningTeamId)

    fun toData(): SwissGameData = SwissGameData(
            id = idProperty.value,
            gameId = gameIdProperty.value,
            winningTeamId = winningTeamIdProperty.value)
}

fun fromSwissGameData(data: SwissGameData): SwissGameItem =
        SwissGameItem(
                id = data.id,
                gameId = data.gameId,
                winningTeamId = data.winningTeamId)
