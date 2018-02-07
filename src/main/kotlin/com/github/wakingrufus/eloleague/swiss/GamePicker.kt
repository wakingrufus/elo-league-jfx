package com.github.wakingrufus.eloleague.swiss

import com.github.wakingrufus.eloleague.game.GameItem
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.ObservableList
import mu.KLogging
import tornadofx.*

class GamePicker : Fragment() {
    companion object : KLogging()

    val games: List<GameItem> by param()
    val selectedGame = SimpleObjectProperty<GameItem>()

    override val root = vbox {
        combobox(values = games, property = selectedGame) {
            cellFormat {
                text = StringBuilder()
                        .apply {
                            append(it.team1Players.joinToString(" & ") { it.name }).append(" ")
                        }.apply {
                            append(it.team1Score).append(" vs ")
                        }.apply {
                            append(it.team2Players.joinToString(" & ") { it.name }).append(" ")
                        }.apply {
                            append(it.team2Score).append(" ")
                        }.toString()
            }
        }
        buttonbar {
            button("Ok") {
                enableWhen(selectedGame.isNotNull)
                action {
                    close()
                }
            }
            button("Cancel") {
                action {
                    selectedGame.value = null
                    close()
                }
            }
        }
    }
}